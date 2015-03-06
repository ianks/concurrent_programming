 import java.util.concurrent.locks.ReentrantLock;

 public class FineGrainList<AnyType> {
	 private final Node head;

	 public FineGrainList() {
	 	head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
	 }

	 public boolean add(AnyType item) {
		 Node pred, curr;
		 int key = item.hashCode();

		 head.lock();
		 pred = head;
		 try {
			 curr = pred.next;
			 curr.lock();
			 try {
				 while (curr.key < key) {
					 pred.unlock();
					 pred = curr;
					 curr = curr.next;
					 curr.lock();
				 }
				 if (key == curr.key){
					 return false;
				 } else {
					 Node node = new Node(item);
					 node.next = curr;
					 pred.next = node;
					 return true;
				 }
			 } finally {
				 curr.unlock();
			 }
		 } finally {
			 pred.unlock();
		 }
	 }

	 public boolean remove(AnyType item) {
		 Node pred, curr;
		 int key = item.hashCode();

		 head.lock();
		 pred = head;
		 try {
			 curr = pred.next;
			 curr.lock();
			 try {
				 while (curr.key < key) {
					 pred.unlock();
					 pred = curr;
					 curr = curr.next;
					 curr.lock();
				 }
				 if (key == curr.key){
					 pred.next = curr.next;
					 return false;
				 } else {
					 return true;
				 }
			 } finally {
				 curr.unlock();
			 }
		 } finally {
			 pred.unlock();
		 }
	 }

	 public boolean contains(AnyType item) {
		 Node pred, curr;
		 int key = item.hashCode();

		 head.lock();
		 pred = head;
		 try {
			 curr = pred.next;
			 curr.lock();
			 try {
				 while (curr.key < key) {
					 pred.unlock();
					 pred = curr;
					 curr = curr.next;
					 curr.lock();
				 }
				 if (key == curr.key){
					 return true;
				 } else {
					 return false;
				 }
			 } finally {
				 curr.unlock();
			 }
		 } finally {
			 pred.unlock();
		 }
	 }

	 public int getSize() {
		 int size = -1;
		 Node pred = head;

		 while ((pred = pred.next) != null)
			 size++;

		 return size;
	 }

	 private class Node {
		 AnyType item;
		 Node next;
		 int key;
	 	 private final ReentrantLock lock = new ReentrantLock();

		 public Node(AnyType item) {
			 item = item;
			 key = item.hashCode();
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
