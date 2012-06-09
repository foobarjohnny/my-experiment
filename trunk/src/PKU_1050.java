public class PKU_1050
{
     private int maxSubArray(int n,int a[])
      {
            int b=0,sum=-10000000;
            for(int i=0;i<n;i++)
            {
                  if(b>0) b+=a[i];
                  else b=a[i];
                  if(b>sum) sum=b;
            }
            return sum;  
      }
      private int maxSubMatrix(int n,int[][] array)
      {
            int i,j,k,max=0,sum=-100000000;
            int b[]=new int[101];
            for(i=0;i<n;i++)
            {
                  for(k=0;k<n;k++)//初始化b[]
                  {
                        b[k]=0;
                  }
                  for(j=i;j<n;j++)//把第i行到第j行相加,对每一次相加求出最大值
                  {
                        for(k=0;k<n;k++)
                        {
                              b[k]+=array[j][k];
                        }
                        max=maxSubArray(k,b);  
                        if(max>sum)
                        {
                                sum=max;
                        }
                  }
            }
            return sum;
      }
      public static void main(String args[])
      {
            PKU_1050 p=new PKU_1050();
            int n =4;
           int[][] a= { {0, -2, -7,  0}, {9, 2, -6, 2}, {-4, 1, -4, 1}, {-1, 8, 0, -2 }}; 
            System.out.println(p.maxSubMatrix(n,a));
            
            
            int N =4;
            int[][] A={ {0, -2, -7,  0}, {9, 2, -6, 2}, {-4, 1, -4, 1}, {-1, 8, 0, -2 }}; 
            int[][] F=new int[N+1][N+1];
            int[] P = new int[N+1];
            int[] Q = new int[N+1];
            
            int max, index_i = 0, index_j = 0;
            // F 
            for(int i=1; i<N+1; i++) {
              F[i][0] = 0; // 第0列，即哨兵列 
              F[i][1] = A[i-1][0]; // 第1列 
            }
            for(int i=1; i<N+1; i++) {
              for(int j=2; j<N+1; j++) { // 从第2列开始 
                F[i][j] = F[i][j-1] + A[i-1][j-1];
              }
            }
            // P,Q
            max = -300000;
            int hi = 0, hj=0;
            for(int i=1; i<N+1; i++) {
              for(int j=i+1; j<N+1; j++) {
                P[1] = F[1][j]-F[1][i-1];
                Q[1] = P[1];
                hj=0;
                hi=0;
                for(int k=2; k<N+1; k++) {
                  P[k] = P[k-1]>0?(P[k-1]+F[k][j]-F[k][i-1]):(F[k][j]-F[k][i-1]);
                  hi = P[k-1]>0?hj:k;
                  Q[k] = Q[k-1]>P[k]?Q[k-1]:P[k]; 
                  hj = Q[k-1]>P[k]?hj:k;
                }
                if(Q[N] > max) {
                  max = Q[N];
                  index_i = i;
                  index_j = j;
                }
              }
            }
            // max
            printMatrix(F, N+1); 
            printMatrix(A,N);
            System.out.println(max);
            System.out.println(index_i+","+index_j);
            System.out.println(hi+","+hj);
      }
      
      public static void printMatrix(int[][] a,int n)
      {
    	  for(int i=0;i<n;i++){
       	   for(int j=0;j<n;j++){
       	    System.out.print(a[i][j]+ "  " );
       	   }
       	   System.out.println();
       	  }
       	  
       	 
      }
}