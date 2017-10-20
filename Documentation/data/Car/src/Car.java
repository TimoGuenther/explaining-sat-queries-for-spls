public class Car {
	public static void main(String[] args) {
		System.out.print("Hello, car");
		// #if Gearbox
		System.out.print(" with");
		// #if Manual
//@		System.out.print(" a manual");
		// #elif Automatic
		System.out.print(" an automatic");
		// #if Manual
//@		System.out.print(" and impossible");
		// #endif
		// #endif
		System.out.print(" gearbox");
		// #endif
		// #if Bluetooth
//@		System.out.print(" and Bluetooth");
		// #endif
		System.out.println("!");
	}
}
