public class Signal {
    String name;
    int countMilisec = 0;
    int countErrors = 0;
    int minMilisecError = 99999999;
    int maxMilisecError = 0;

    Signal(String n){

        name = n;
    }

    public int GetCountErrors(){
        return countErrors;
    }

    public void AddValue(int m){
        if(m == 1){
            countMilisec += 1;
        } else if(countMilisec> 0 && m == 0){
            countErrors++;
            if(countMilisec < minMilisecError){
                minMilisecError = countMilisec;
            }
            if(countMilisec > maxMilisecError){
                maxMilisecError = countMilisec;
            }
            countMilisec = 0;
        }
    }
}
