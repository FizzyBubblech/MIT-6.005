package tutorial;

public class CamelCase {

}

class GravityCalculator {
    
    public static void main(String[] arguments) {
        double gravity = -9.81; // Earth's gravity in m/s^2
        double initialVelocity = 0.0;
        double fallingTime = 10.0;
        double initialPosition = 0.0;
        double finalPosition = 0.5 * gravity * Math.pow(fallingTime, 2) + 
                (initialVelocity * fallingTime) + initialPosition;
        
        System.out.println("The object's position after " + fallingTime +
                " seconds is " + finalPosition + " m.");
    }
}
// The output of unmodified program is 'The object's position after 10.0 seconds is 0.0 m.'

class FooCorporation {
    
    public static void totalPay(double basePay, int workHours) {
        if (basePay < 8.00) {
            System.out.println("Must be paid at least $8.00/hour");
        }
        else if (workHours > 60) {
            System.out.println("Can't work more than 60 hours a week");
        }
        else if (workHours > 40) {
            double base = basePay * 40;
            double overtime = (basePay * 1.5) * (workHours - 40);
            System.out.println("The total pay is: $" + (overtime + base));
        }
        else {
            System.out.println("The total pay is: $" + (basePay * workHours));
        }
    }
    
    public static void main(String[] arguments) {
        totalPay(7.50, 35);
        totalPay(8.20, 47);
        totalPay(10, 73);
    }
}

class Marathon {
    
    public static int getMinIndex(int[] times) {
        int minIndex = 0;
        for (int i=0; i < times.length; i++) {
            if(times[i] < times[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }
    
    public static int getSecondMinIndex(int[] times) {
        int minIndex = getMinIndex(times);
        int secondIndex = 0;
        for (int i=0; i < times.length; i++) {
            if (i == minIndex) {
                continue;
            }
            if(times[i] < times[secondIndex]) {
                secondIndex = i;
            }
        }
        return secondIndex;
    }
    
    public static void main(String[] arguments) {
        String[] names ={
                "Elena", "Thomas", "Hamilton", "Suzie", "Phil", "Matt", "Alex",
                "Emma", "John", "James", "Jane", "Emily", "Daniel", "Neda",
                "Aaron", "Kate"
        };
        int[] times ={
                341, 273, 278, 329, 445, 402, 388, 275, 243, 334, 412, 393, 299,
                343, 317, 265
        };
        
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]+ ": " + times[i]);
        }
        
        int first = getMinIndex(times);
        int second = getSecondMinIndex(times);
        
        System.out.println("The lowest time is " + times[first] + 
                " minutes run by " + names[first] + '.');
        System.out.println("The second lowest time is " + times[second] + 
                " minutes run by " + names[second] + '.');
    }
}