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
package zakadabar.lib.lucene.business.search

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.Term
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.*
import org.apache.lucene.store.FSDirectory
import zakadabar.lib.lucene.business.index.IndexFiles
import zakadabar.lib.lucene.business.index.knn.DemoEmbeddings
import zakadabar.lib.lucene.business.index.knn.KnnVectorDict
import zakadabar.lib.lucene.data.LuceneQuery
import zakadabar.lib.lucene.data.LuceneQueryResult
import zakadabar.lib.lucene.data.LuceneSettings
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/** Simple command-line based search demo.  */
object SearchFiles {
    /** Simple command-line based search demo.  */
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val usage =
            "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage] [-knn_vector knnHits]\n\nSee http://lucene.apache.org/core/9_0_0/demo/ for details."
        if (args.size > 0 && ("-h" == args[0] || "-help" == args[0])) {
            println(usage)
            System.exit(0)
        }
        var index = "index"
        var field = "contents"
        var queries: String? = null
        var repeat = 0
        var raw = false
        var knnVectors = 0
        var queryString: String? = null
        var hitsPerPage = 10
        var i = 0
        while (i < args.size) {
            when (args[i]) {
                "-index" -> index = args[++ i]
                "-field" -> field = args[++ i]
                "-queries" -> queries = args[++ i]
                "-query" -> queryString = args[++ i]
                "-repeat" -> repeat = args[++ i].toInt()
                "-raw" -> raw = true
                "-paging" -> {
                    hitsPerPage = args[++ i].toInt()
                    if (hitsPerPage <= 0) {
                        System.err.println("There must be at least 1 hit per page.")
                        System.exit(1)
                    }
                }
                "-knn_vector" -> knnVectors = args[++ i].toInt()
                else -> {
                    System.err.println("Unknown argument: " + args[i])
                    System.exit(1)
                }
            }
            i ++
        }
        val reader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(index)))
        val searcher = IndexSearcher(reader)
        val analyzer: Analyzer = StandardAnalyzer()
        var vectorDict: KnnVectorDict? = null
        if (knnVectors > 0) {
            vectorDict = KnnVectorDict(reader.directory(), IndexFiles.KNN_DICT)
        }
        val `in`: BufferedReader
        `in` = if (queries != null) {
            Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8)
        } else {
            BufferedReader(InputStreamReader(System.`in`, StandardCharsets.UTF_8))
        }
        val parser = QueryParser(field, analyzer)
        while (true) {
            if (queries == null && queryString == null) { // prompt the user
                println("Enter query: ")
            }
            var line = queryString ?: `in`.readLine() ?: break
            line = line.trim { it <= ' ' }
            if (line.isEmpty()) {
                break
            }
            var query: Query = parser.parse(line)
            if (knnVectors > 0) {
                query = addSemanticQuery(query, vectorDict, knnVectors)
            }
            println("Searching for: " + query.toString(field))
            if (repeat > 0) { // repeat & time as benchmark
                val start = Date()
                for (i in 0 until repeat) {
                    searcher.search(query, 100)
                }
                val end = Date()
                println("Time: " + (end.time - start.time) + "ms")
            }
            doPagingSearch(searcher, query, hitsPerPage, raw, queries == null && queryString == null)
            if (queryString != null) {
                break
            }
        }
        vectorDict?.close()
        reader.close()
    }

    fun search(luceneQuery: LuceneQuery, settings: LuceneSettings): List<LuceneQueryResult> {
        val index = settings.index
        val knnVectors = luceneQuery.knnVectors

        var vectorDict: KnnVectorDict? = null
        val reader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(index)))

        try {
            val searcher = IndexSearcher(reader)
            val analyzer: Analyzer = StandardAnalyzer()
            if (knnVectors > 0) {
                vectorDict = KnnVectorDict(reader.directory(), IndexFiles.KNN_DICT)
            }
            val parser = QueryParser(luceneQuery.field, analyzer)

            val line = luceneQuery.query.trim { it <= ' ' }
            require(line.isNotEmpty()) { "query string shall not be empty" }

            var query: Query = parser.parse(line)
            if (knnVectors > 0) {
                query = addSemanticQuery(query, vectorDict, knnVectors)
            }

            return doPagingSearch(searcher, query, luceneQuery.hitsPerPage, false, false)
        } finally {
            vectorDict?.close()
            reader.close()
        }
    }

    /**
     * This demonstrates a typical paging search scenario, where the search engine presents pages of
     * size n to the user. The user can then go to the next page if interested in the next hits.
     *
     *
     * When the query is executed for the first time, then only enough results are collected to
     * fill 5 result pages. If the user wants to page beyond this limit, then the query is executed
     * another time and all hits are collected.
     */
    @Throws(IOException::class)
    fun doPagingSearch(
        searcher: IndexSearcher,
        query: Query?,
        hitsPerPage: Int,
        raw: Boolean,
        interactive: Boolean
    ): List<LuceneQueryResult> {

        // FIXME check details of paging, check 'main' about how to do it

        val hits = searcher.search(query, 5 * hitsPerPage)

        return hits.scoreDocs.map {
            val doc = searcher.doc(it.doc)
            LuceneQueryResult(
                path = doc["path"],
                title = doc["title"]
            )
        }
    }

    @Throws(IOException::class)
    private fun addSemanticQuery(query: Query, vectorDict: KnnVectorDict?, k: Int): Query {
        val semanticQueryText = StringBuilder()
        val termExtractor = QueryFieldTermExtractor("contents")
        query.visit(termExtractor)
        for (term in termExtractor.terms) {
            semanticQueryText.append(term).append(' ')
        }
        if (semanticQueryText.length > 0) {
            val knnQuery = KnnVectorQuery(
                "contents-vector",
                vectorDict?.let { DemoEmbeddings(it).computeEmbedding(semanticQueryText.toString()) },
                k
            )
            val builder: BooleanQuery.Builder = BooleanQuery.Builder()
            builder.add(query, BooleanClause.Occur.SHOULD)
            builder.add(knnQuery, BooleanClause.Occur.SHOULD)
            return builder.build()
        }
        return query
    }

    private class QueryFieldTermExtractor internal constructor(private val field: String) : QueryVisitor() {
        val terms: MutableList<String> = ArrayList()
        override fun acceptField(field: String): Boolean {
            return field == this.field
        }

        override fun consumeTerms(query: Query, vararg terms: Term) {
            for (term in terms) {
                this.terms.add(term.text())
            }
        }

        override fun getSubVisitor(occur: BooleanClause.Occur, parent: Query): QueryVisitor {
            return if (occur === BooleanClause.Occur.MUST_NOT) {
                QueryVisitor.EMPTY_VISITOR
            } else this
        }
    }
}