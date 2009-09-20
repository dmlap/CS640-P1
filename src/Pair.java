
public class Pair <T, U>
{
	private final T first;
	private final U second;
	private transient final int hash;
	
	public Pair( T f, U s)
	{
		this.first = f;
		this.second = s;
		hash = (first == null? 0 : first.hashCode() * 31) + (second == null? 0 : second.hashCode());
	}
	
	public T getFirst()
	{
		return first;
	}
		
	public U getSecond()
	{
		return second;
	}
	
	@Override
	public int hashCode()
	{
		return hash;
	}
}
