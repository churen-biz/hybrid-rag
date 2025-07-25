package biz.churen.ee.rag.api.service.rank;

import java.util.List;

public interface RankService {

    /**
     * Rank the given passages based on their relevance to the query.
     */
    List<Integer> score(String query, List<String> passages);

    List<Integer> scoreV2(String query, List<String> passages);
}
