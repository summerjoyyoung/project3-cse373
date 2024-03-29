package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> result = new ChainedHashDictionary<>();
        ISet<URI> links = new ChainedHashSet<>();
        for (Webpage webpage : webpages) {
            links.add(webpage.getUri());
        }
        for (Webpage webpage : webpages) {
            URI pageURI = webpage.getUri();
            if (!result.containsKey(pageURI)) {
                result.put(pageURI, new ChainedHashSet<URI>());
            }
            for (URI link : webpage.getLinks()) {
                if (links.contains(link)) {
                    result.get(pageURI).add(link);
                }
            }
        }
        return result;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        IDictionary<URI, Double> newValue = new ChainedHashDictionary<>();
        IDictionary<URI, Double> oldValue = new ChainedHashDictionary<>();
        
        for (KVPair<URI, ISet<URI>> page : graph) {
            URI pageName = page.getKey();
            oldValue.put(pageName, 1.0/graph.size());
            newValue.put(pageName, 0.0);
        }
        int processed = 1;  
        int i = 0; 
        while (i < limit && processed > 0) {
            processed = 0;
            for (KVPair<URI, ISet<URI>> page : graph) {
                ISet<URI> pageLinks = page.getValue();
                URI pageName = page.getKey();
                if (pageLinks.isEmpty()) {
                    for (KVPair<URI, Double> link : newValue) {
                        URI linkName = link.getKey(); 
                        newValue.put(linkName, newValue.get(linkName) + 
                                (decay * oldValue.get(pageName)) / graph.size()); 
                    }
                } else { 
                    for (URI link : pageLinks) {
                        newValue.put(link, newValue.get(link) + (decay * oldValue.get(pageName)) / pageLinks.size());
                    }
                } 
            }
            for (KVPair<URI, Double> page : newValue) { 
                URI pageName = page.getKey(); 
                newValue.put(pageName, newValue.get(pageName) + (1 - decay) / graph.size());
                if (Math.abs(newValue.get(pageName) - oldValue.get(pageName)) > epsilon) { 
                    processed++;
                } 
                oldValue.put(pageName, newValue.get(pageName));
                newValue.put(pageName, 0.0);
            } 
            i++;  
        }
        return oldValue;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return this.pageRanks.get(pageUri);
    }
}
