
public class SimpleStack<E> implements StackADT<E>{
	private E items[];
	private int numItems;
	private static final int INITSIZE = 10;
	public SimpleStack(){
		this.items = (E[])(new Object[INITSIZE]);
		this.numItems = 0;
	}
	@Override
	public boolean isEmpty() {
		return this.numItems==0;
	}

	@Override
	public E peek() throws EmptyStackException {
		if(isEmpty()) throw new EmptyStackException();
		return this.items[this.numItems-1];
	}

	@Override
	public E pop() throws EmptyStackException {
		if(isEmpty()) throw new EmptyStackException();
		E returned = this.items[this.numItems-1];
		this.items[this.numItems-1] = null;
		this.numItems--;
		return returned;
	}

	@Override
	public void push(E item) {
		if(this.items.length==this.numItems){
			E[] newItems = (E[])(new Object[2*this.items.length]);
			for(int i = 0; i < this.items.length; i++)
				newItems[i] = this.items[i];
			this.items = newItems;
		}
		this.items[this.numItems] = item;
		this.numItems++;
	}

	@Override
	public int size() {
		return this.numItems;
	}
	
	public String toString(){
		String theItems = "";
		for(int i = this.numItems-1; i >= 0; i--)
			theItems = theItems + this.items[i].toString() + "\n";
		return theItems;
	}
	
}
