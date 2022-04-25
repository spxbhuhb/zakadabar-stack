/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zakadabar.lib.lucene.business.index.knn

import org.apache.lucene.store.Directory
import org.apache.lucene.store.IOContext
import org.apache.lucene.store.IndexInput
import org.apache.lucene.store.IndexOutput
import org.apache.lucene.util.BytesRef
import org.apache.lucene.util.IntsRefBuilder
import org.apache.lucene.util.VectorUtil
import org.apache.lucene.util.fst.FST
import org.apache.lucene.util.fst.FSTCompiler
import org.apache.lucene.util.fst.PositiveIntOutputs
import org.apache.lucene.util.fst.Util
import java.io.BufferedReader
import java.io.Closeable
import java.io.IOException
import java.lang.Float
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern
import kotlin.Array
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.FloatArray
import kotlin.IllegalArgumentException
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Throws
import kotlin.check
import kotlin.require

/**
 * Manages a map from token to numeric vector for use with KnnVector indexing and search. The map is
 * stored as an FST: token-to-ordinal plus a dense binary file holding the vectors.
 */
class KnnVectorDict(directory: Directory, dictName: String) : Closeable {
    private var fst: FST<Long>? = null
    private val vectors: IndexInput

    /**
     * Get the dimension of the vectors returned by this.
     *
     * @return the vector dimension
     */
    val dimension: Int

    /**
     * Sole constructor
     *
     * @param directory Lucene directory from which knn directory should be read.
     * @param dictName the base name of the directory files that store the knn vector dictionary. A
     * file with extension '.bin' holds the vectors and the '.fst' maps tokens to offsets in the
     * '.bin' file.
     */
    init {
        directory.openInput("$dictName.fst", IOContext.READ).use { fstIn -> fst = FST(fstIn, fstIn, PositiveIntOutputs.getSingleton()) }
        vectors = directory.openInput("$dictName.bin", IOContext.READ)
        val size = vectors.length()
        vectors.seek(size - Integer.BYTES)
        dimension = vectors.readInt()
        check((size - Integer.BYTES) % (dimension * Float.BYTES) == 0L) { "vector file size $size is not consonant with the vector dimension $dimension" }
    }

    /**
     * Get the vector corresponding to the given token. NOTE: the returned array is shared and its
     * contents will be overwritten by subsequent calls. The caller is responsible to copy the data as
     * needed.
     *
     * @param token the token to look up
     * @param output the array in which to write the corresponding vector. Its length must be [     ][.getDimension] * [Float.BYTES]. It will be filled with zeros if the token is not
     * present in the dictionary.
     * @throws IllegalArgumentException if the output array is incorrectly sized
     * @throws IOException if there is a problem reading the dictionary
     */
    @Throws(IOException::class)
    operator fun get(token: BytesRef?, output: ByteArray) {
        require(output.size == dimension * Float.BYTES) {
            ("the output array must be of length "
                    + dimension * Float.BYTES
                    + ", got "
                    + output.size)
        }
        val ord = Util.get(fst, token)
        if (ord == null) {
            Arrays.fill(output, 0.toByte())
        } else {
            vectors.seek(ord * dimension * java.lang.Float.BYTES)
            vectors.readBytes(output, 0, output.size)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        vectors.close()
    }

    private class Builder {
        private val intsRefBuilder = IntsRefBuilder()
        private val fstCompiler = FSTCompiler(FST.INPUT_TYPE.BYTE1, PositiveIntOutputs.getSingleton())
        private lateinit var scratch: FloatArray
        private var byteBuffer: ByteBuffer? = null
        private var ordinal: Long = 1
        private var numFields = 0
        @Throws(IOException::class)
        fun build(gloveInput: Path?, directory: Directory, dictName: String) {
            Files.newBufferedReader(gloveInput).use { `in` ->
                directory.createOutput("$dictName.bin", IOContext.DEFAULT).use { binOut ->
                    directory.createOutput("$dictName.fst", IOContext.DEFAULT).use { fstOut ->
                        writeFirstLine(`in`, binOut)
                        while (addOneLine(`in`, binOut)) {
                            // continue;
                        }
                        fstCompiler.compile().save(fstOut, fstOut)
                        binOut.writeInt(numFields - 1)
                    }
                }
            }
        }

        @Throws(IOException::class)
        private fun writeFirstLine(`in`: BufferedReader, out: IndexOutput) {
            val fields = readOneLine(`in`) ?: return
            numFields = fields.size
            byteBuffer = ByteBuffer.allocate((numFields - 1) * java.lang.Float.BYTES).order(ByteOrder.LITTLE_ENDIAN)
            scratch = FloatArray(numFields - 1)
            writeVector(fields, out)
        }

        @Throws(IOException::class)
        private fun readOneLine(`in`: BufferedReader): Array<String>? {
            val line = `in`.readLine() ?: return null
            return SPACE_RE.split(line, 0)
        }

        @Throws(IOException::class)
        private fun addOneLine(`in`: BufferedReader, out: IndexOutput): Boolean {
            val fields = readOneLine(`in`) ?: return false
            check(fields.size == numFields) {
                ("different field count at line "
                        + ordinal
                        + " got "
                        + fields.size
                        + " when expecting "
                        + numFields)
            }
            fstCompiler.add(Util.toIntsRef(BytesRef(fields[0]), intsRefBuilder), ordinal ++)
            writeVector(fields, out)
            return true
        }

        @Throws(IOException::class)
        private fun writeVector(fields: Array<String>, out: IndexOutput) {
            byteBuffer !!.position(0)
            val floatBuffer = byteBuffer !!.asFloatBuffer()
            for (i in 1 until fields.size) {
                scratch[i - 1] = fields[i].toFloat()
            }
            VectorUtil.l2normalize(scratch)
            floatBuffer.put(scratch)
            val bytes = byteBuffer !!.array()
            out.writeBytes(bytes, bytes.size)
        }

        companion object {
            private val SPACE_RE = Pattern.compile(" ")
        }
    }

    /** Return the size of the dictionary in bytes  */
    fun ramBytesUsed(): Long {
        return fst !!.ramBytesUsed() + vectors.length()
    }

    companion object {
        /**
         * Convert from a GloVe-formatted dictionary file to a KnnVectorDict file pair.
         *
         * @param gloveInput the path to the input dictionary. The dictionary is delimited by newlines,
         * and each line is space-delimited. The first column has the token, and the remaining columns
         * are the vector components, as text. The dictionary must be sorted by its leading tokens
         * (considered as bytes).
         * @param directory a Lucene directory to write the dictionary to.
         * @param dictName Base name for the knn dictionary files.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun build(gloveInput: Path?, directory: Directory, dictName: String) {
            Builder().build(gloveInput, directory, dictName)
        }
    }
}