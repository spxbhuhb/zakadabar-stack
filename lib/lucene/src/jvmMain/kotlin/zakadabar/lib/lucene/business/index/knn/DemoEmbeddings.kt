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

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LowerCaseFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.standard.StandardTokenizer
import java.io.IOException
import java.io.Reader
import java.io.StringReader

/**
 * This class provides [.computeEmbedding] and [.computeEmbedding] for
 * calculating "semantic" embedding vectors for textual input.
 */
class DemoEmbeddings(vectorDict: KnnVectorDict) {
    private val analyzer: Analyzer

    init {
        analyzer = object : Analyzer() {
            override fun createComponents(fieldName: String): TokenStreamComponents {
                val tokenizer: Tokenizer = StandardTokenizer()
                val output: TokenStream = KnnVectorDictFilter(LowerCaseFilter(tokenizer), vectorDict)
                return TokenStreamComponents(tokenizer, output)
            }
        }
    }

    /**
     * Tokenize and lower-case the input, look up the tokens in the dictionary, and sum the token
     * vectors. Unrecognized tokens are ignored. The resulting vector is normalized to unit length.
     *
     * @param input the input to analyze
     * @return the KnnVector for the input
     */
    @Throws(IOException::class)
    fun computeEmbedding(input: String): FloatArray {
        return computeEmbedding(StringReader(input))
    }

    /**
     * Tokenize and lower-case the input, look up the tokens in the dictionary, and sum the token
     * vectors. Unrecognized tokens are ignored. The resulting vector is normalized to unit length.
     *
     * @param input the input to analyze
     * @return the KnnVector for the input
     */
    @Throws(IOException::class)
    fun computeEmbedding(input: Reader?): FloatArray {
        analyzer.tokenStream("dummyField", input).use { tokens ->
            tokens.reset()
            @Suppress("ControlFlowWithEmptyBody")
            while (tokens.incrementToken()) {  }
            tokens.end()
            return (tokens as KnnVectorDictFilter).result
        }
    }
}