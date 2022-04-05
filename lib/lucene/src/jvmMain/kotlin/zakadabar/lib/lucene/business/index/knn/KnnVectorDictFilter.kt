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

import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.util.BytesRef
import org.apache.lucene.util.VectorUtil
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

/**
 * Looks up each tokens in a dictionary, and sums the token vectors. Unrecognized tokens are
 * ignored. The resulting vector is normalized to unit length.
 */
class KnnVectorDictFilter(input: TokenStream?, private val dict: KnnVectorDict) : TokenFilter(input) {
    private val termAtt = addAttribute(CharTermAttribute::class.java)
    private val scratchFloats: FloatArray

    /**
     * Get the vector computed from the input
     *
     * @return the resultant sum of the vectors of each term.
     */
    val result: FloatArray
    private val scratchBytes: ByteArray
    private val scratchBuffer: FloatBuffer

    /**
     * sole constructor
     *
     * @param input the input token stream to filter.
     * @param dict a token to vector dictionary, used to look up the token vectors.
     */
    init {
        result = FloatArray(dict.dimension)
        scratchBytes = ByteArray(dict.dimension * java.lang.Float.BYTES)
        scratchBuffer = ByteBuffer.wrap(scratchBytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer()
        scratchFloats = FloatArray(dict.dimension)
    }

    @Throws(IOException::class)
    override fun incrementToken(): Boolean {
        if (input.incrementToken() == false) {
            return false
        }
        val term = BytesRef(termAtt.toString())
        dict[term, scratchBytes]
        scratchBuffer.position(0)
        scratchBuffer[scratchFloats]
        VectorUtil.add(result, scratchFloats)
        return true
    }

    @Throws(IOException::class)
    override fun reset() {
        super.reset()
        Arrays.fill(result, 0f)
    }

    @Throws(IOException::class)
    override fun end() {
        super.end()
        VectorUtil.l2normalize(result, false)
    }
}