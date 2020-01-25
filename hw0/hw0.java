public class hw0{

  public static void main(String[] args){
    System.out.println(max(new int[] {1,2,3}));
    System.out.println(threeSum(new int[] {-2, -5, 5, 2}));
    System.out.println(threeSum(new int[] {-2, -5, 5, 7}));
    System.out.println(threeSumDistinct(new int[] {0, 0, 1, 2}));
    System.out.println(threeSumDistinct(new int[] {0, -1, 1, 3}));
  }
  public static int max(int[] arr){
    int max_num = arr[0];
    for (int i = 0; i < arr.length; i++){
      if (arr[i] > max_num) max_num = arr[i];
    }
    return max_num;
  }

  public static boolean threeSum(int[] arr){
    for (int i = 0; i < arr.length; i++){
      for (int j = 0; j < arr.length; j++){
        for (int k = 0; k < arr.length; k++){
          if (arr[i] + arr[j] + arr[k] == 0) return true;
        }
      }
    }
    return false;
  }

  public static boolean threeSumDistinct(int[] arr){
    for (int i = 0; i < arr.length; i++){
      for (int j = i + 1; j < arr.length; j++){
        for (int k = j + 1; k < arr.length; k++){
          if (arr[i] + arr[j] + arr[k] == 0) return true;
        }
      }
    }
    return false;
  }


}
