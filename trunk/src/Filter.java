import java.util.List;  
  
public interface Filter<T>  
{  
    public void accept(List<T> list, T t);  
  
}  