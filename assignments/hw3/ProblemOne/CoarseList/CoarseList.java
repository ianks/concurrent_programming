 import java.util.concurrent.locks.ReentrantLock;

 public class CoarseList<AnyType> {
	 private final Node head;
	 private final ReentrantLock lock = new ReentrantLock();

	 public CoarseList() {
	 	head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
	 }

	 public boolean add(AnyType item) {
		 Node pred, curr;
		 int key = item.hashCode();

		 lock.lock();
		 try {
			 pred = head;
			 curr = pred.next;
			 while (curr.key < key) {
				 pred = curr;
				 curr = curr.next;
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
			 lock.unlock();
		 }
	 }

	 public boolean remove(AnyType item) {
		 Node pred, curr;
		 int key = item.hashCode();

		 lock.lock();
		 try {
			 pred = head;
			 curr = pred.next;
			 while (curr.key < key){
				 pred = curr;
				 curr = curr.next;
			 }
			 if (key == curr.key) {
				 pred.next = curr.next;
				 return true;
			 } else {
				 return false;
			 }
		 } finally {
			 lock.unlock();
		 }
	 }

	 public int getSize() {
		 int size = -1;
		 Node pred = head;

		 while ((pred = pred.next) != null)
			 size++;

		 return size;
	 }

	 public boolean contains(AnyType item) {
		 Node pred, curr;
		 int key = item.hashCode();

		 lock.lock();
		 try {
			 pred = head;
			 curr = pred.next;
			 while (curr.key < key){
				 pred = curr;
				 curr = curr.next;
			 }
			 if (key == curr.key) {
				 return true;
			 } else {
				 return false;
			 }
		 } finally {
			 lock.unlock();
		 }
	 }


	 // This isn't thread safe?? WTF. It is only called within critical
	 // section...
	 private boolean findNode(int key, Node pred, Node curr){
		 while (curr.key < key) {
			 pred = curr;
			 curr = curr.next;
		 }

		 return key == curr.key;
	 }

	 private class Node {
		 AnyType item;
		 Node next;
		 int key;

		 public Node(AnyType item) {
			 item = item;
			 key = item.hashCode();
		 }

		 public Node(Integer data){
			 key = data;
		 }
	 }
 }
