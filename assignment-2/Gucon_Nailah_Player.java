class Gucon_Nailah_Player extends Player {
    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {

        // Count the number of times the opponents defected and cooperated in the past
        int oppDefections1 = 0, oppDefections2 = 0, oppCooperations1 = 0, oppCooperations2 = 0;
        for (int i = 0; i < n; i++) {
            if (oppHistory1[i] == 1) oppDefections1++;
            else oppCooperations1++;
            if (oppHistory2[i] == 1) oppDefections2++;
            else oppCooperations2++;
        }

        // Calculate the cooperation ratio of the opponents
        double oppCooperationRatio1 = 1.0 * oppCooperations1 / (oppCooperations1 + oppDefections1);
        double oppCooperationRatio2 = 1.0 * oppCooperations2 / (oppCooperations2 + oppDefections2);

        if (n == 0) {
            return 0; // Always cooperate in the first round.
        } else if (n <= 10) {
            // for the first 10 rounds, use refined version of JOSS
            // if oppCooperationRatio of both opponents is more than 0.8, cooperate
            if (oppCooperationRatio1 >= 0.8 && oppCooperationRatio2 >= 0.8) {
                return 0;
            } else {
                // cooperate with a 90% probability and defect with a 10% probability
                return Math.random() < 0.9 ? 0 : 1;
            }
        } else {
            // calculate defect rate of both opponents
            double oppDefectRate1 = 1.0 * oppDefections1 / n;
            double oppDefectRate2 = 1.0 * oppDefections2 / n;

            // handle untrustworthy opponent with defection
            if (oppDefectRate1 >= 0.2 || oppDefectRate2 >= 0.2) {
                return 1;
            }

            // handle trustworthy opponents with cooperation
            if (oppDefectRate1 < 0.05 && oppDefectRate2 < 0.05) {
                return 0;
            }

            // Check if both opponents have cooperated in the last round
            if (oppHistory1[n - 1] == 0 && oppHistory2[n - 1] == 0) {
                // Check if an opponent has defected in the round before that
                if (oppHistory1[n - 2] == 1 || oppHistory2[n - 2] == 1) {
                    // Defect in response to recent defection
                    return 1;
                } else {
                    // Cooperate if both opponents have been mostly cooperative
                    return 0;
                }
            } else {
                // If either opponent defected in the last round, punish them by defecting
                return 1;
            }
        }
    }
}