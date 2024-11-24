package idd.application;

import java.util.List;

public class EvaluationMetrics {

    public static double calculateMRR(List<List<Integer>> rankedResults, List<Integer> relevantDocuments) {
        double mrr = 0.0;
        int queryCount = rankedResults.size();

        for (List<Integer> results : rankedResults) {
            for (int rank = 0; rank < results.size(); rank++) {
                if (relevantDocuments.contains(results.get(rank))) {
                    mrr += 1.0 / (rank + 1);
                    break;
                }
            }
        }

        return mrr / queryCount;
    }

    public static double calculateNDCG(List<Integer> rankedResults, List<Integer> relevantDocuments, int k) {
        double dcg = 0.0;
        double idcg = calculateIDCG(relevantDocuments, k);

        for (int i = 0; i < k; i++) {
            int docId = rankedResults.get(i);
            if (relevantDocuments.contains(docId)) {
                dcg += 1.0 / (Math.log(i + 2) / Math.log(2));
            }
        }

        return dcg / idcg;
    }

    private static double calculateIDCG(List<Integer> relevantDocuments, int k) {
        double idcg = 0.0;
        for (int i = 0; i < k && i < relevantDocuments.size(); i++) {
            idcg += 1.0 / (Math.log(i + 2) / Math.log(2));
        }
        return idcg;
    }
}
