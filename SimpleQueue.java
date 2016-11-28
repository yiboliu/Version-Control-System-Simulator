
public class SimpleQueue<E> implements QueueADT<E>{
	private E items[];
	private int numItems;
	private int frontIndex;
	private int rearIndex;
	private static final int INITSIZE = 10;
	public SimpleQueue(){
		this.items = (E[])(new Object[INITSIZE]);
		this.numItems = 0;
		this.frontIndex = 0;
		this.rearIndex = this.frontIndex + this.numItems - 1;
	}
	@Override
	public boolean isEmpty() {
		return this.numItems==0;
	}

	@Override
	public E dequeue() throws EmptyQueueException {
		if(isEmpty()) throw new EmptyQueueException();
		E dequeued = this.items[this.frontIndex];
		this.numItems--;
		this.frontIndex++;
		return dequeued;
	}

	@Override
	public void enqueue(E item) {
		if(this.items.length==this.numItems){
			E[] newItems = (E[])(new Object[this.items.length*2]);
			System.arraycopy(this.items, this.frontIndex, newItems, this.frontIndex, this.items.length-this.frontIndex);
			if(this.frontIndex != 0) System.arraycopy(this.items, 0, newItems, this.items.length, this.frontIndex);
			this.items = newItems;
			this.rearIndex = this.frontIndex + this.numItems-1;
		}
		this.rearIndex++;
		this.items[this.rearIndex] = item;
		this.numItems++;
	}

	@Override
	public E peek() throws EmptyQueueException {
		if(isEmpty()) throw new EmptyQueueException();
		return this.items[this.frontIndex];
	}

	@Override
	public int size() {
		return this.numItems;
	}
	
	public String toString(){
		String theItems = "";
		for(int i = 0; i < this.numItems; i++)
			theItems = theItems + this.items[i].toString() + "\n";
		return theItems;
	}
}
