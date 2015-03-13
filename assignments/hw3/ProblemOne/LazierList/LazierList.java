 import java.util.concurrent.locks.ReentrantLock;

 public class LazierList<AnyType> {
	 private final Node head;

	 public LazierList() {
	 	head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
	 }

	 public boolean add(AnyType item) {
		 int key = item.hashCode();

		 while (true) {
			 Node pred = head;
			 Node curr = head.next;

			 while (curr.key < key) {
				 pred = curr;
				 curr = curr.next;
			 }

			 pred.lock();

			 try {
				 curr.lock();

				 try {
					 if (validate(pred, curr)) {
						 if (curr.key == key) {
							 return false;
						 } else {
							 Node node = new Node(item);
							 node.next = curr;
							 pred.next = node;
							 return true;
						 }
					 }
				 } finally {
					 curr.unlock();
				 }

			 } finally {
				 pred.unlock();
			 }
		 }
	 }

	 public boolean remove(AnyType item) {
		 int key = item.hashCode();
		 int indexer = 0;

		 while (true) {
			 Node pred = head;
			 Node curr = head.next;

			 while (curr.key < key) {
				 pred = curr;
				 curr = curr.next;
			 }

			 pred.lock();

			 try {
				 curr.lock();

				 try {
					 if (validate(pred, curr)) {
						 if (curr.key == key) {
							 curr.marked = true;

							 // Only unlink old nodes every 100 times
					 		 if (indexer % 100 == 0)
								 cleanUp();

					 		 indexer++;

							 return true;
						 } else {
							 return false;
						 }
					 }
				 } finally {
					 curr.unlock();
				 }

			 } finally {
				 pred.unlock();
			 }
		 }
	 }

	 public boolean contains(AnyType item) {
		 int key = item.hashCode();
		 Node curr = head;

		 while (curr.key < key)
			 curr = curr.next;

		 return curr.key == key && !curr.marked;
	 }

	 private boolean validate(Node pred, Node curr) {
		 return !pred.marked && !curr.marked && pred.next == curr;
	 }

	 private void cleanUp() {
		 Node pred = null;
		 Node curr = head;

		 while (curr != null) {
			 if (curr.marked)
				 pred.next = curr.next;

			 pred = curr;
			 curr = curr.next;
		 }
	 }

	 public int getSize() {
		 int size = -1;
		 Node curr = head;

		 while ((curr = curr.next) != null)
			 if (!curr.marked) size++;

		 return size;
	 }

	 private class Node {
		 AnyType item;
		 Node next;
		 int key;
		 boolean marked;
	 	 private final ReentrantLock lock = new ReentrantLock();

		 public Node(AnyType item) {
			 item = item;
			 key = item.hashCode();
			 marked = false;
		 }

		 public Node(Integer data){
			 key = data;
		 }

		 public void lock() {
			 lock.lock();
		 }

		 public void unlock() {
			 lock.unlock();
		 }
	 }
 }
