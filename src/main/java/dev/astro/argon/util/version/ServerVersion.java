package dev.astro.argon.util.version;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ServerVersion {

	MINECRAFT_1_21(767, 40, "1.21"),
	MINECRAFT_1_20_5(766, 39, "1.20.5 - 1.20.6"),
	MINECRAFT_1_20_3(765, 38, "1.20.3 - 1.20.4"),
	MINECRAFT_1_20_2(764, 37, "1.20.2"),
	MINECRAFT_1_20(763, 36, "1.20 - 1.20.1"),
	MINECRAFT_1_19_4(762, 35, "1.19.4"),
	MINECRAFT_1_19_3(761, 34, "1.19.3"),
	MINECRAFT_1_19_1(760, 33, "1.19.1 - 1.19.2"),
	MINECRAFT_1_19(759, 32, "1.19"),
	MINECRAFT_1_18_2(758, 31, "1.18.2"),
	MINECRAFT_1_18(757, 30, "1.18 - 1.18.1"),
	MINECRAFT_1_17_1(756, 29, "1.17.1"),
	MINECRAFT_1_17(755, 28, "1.17"),
	MINECRAFT_1_16_4(754, 27, "1.16.4 - 1.16.5"),
	MINECRAFT_1_16_3(753, 26, "1.16.3"),
	MINECRAFT_1_16_2(751, 25, "1.16.2"),
	MINECRAFT_1_16_1(736, 24, "1.16.1"),
	MINECRAFT_1_16(735, 23, "1.16"),
	MINECRAFT_1_15_2(578, 22, "1.15.2"),
	MINECRAFT_1_15_1(575, 21, "1.15.1"),
	MINECRAFT_1_15(573, 20, "1.15"),
	MINECRAFT_1_14_4(498, 19, "1.14.4"),
	MINECRAFT_1_14_3(490, 18, "1.14.3"),
	MINECRAFT_1_14_2(485, 17, "1.14.2"),
	MINECRAFT_1_14_1(480, 16, "1.14.1"),
	MINECRAFT_1_14(477, 15, "1.14"),
	MINECRAFT_1_13_2(404, 14, "1.13.2"),
	MINECRAFT_1_13_1(401, 13, "1.13.1"),
	MINECRAFT_1_13(393, 12, "1.13"),
	MINECRAFT_1_12_2(340, 11, "1.12.2"),
	MINECRAFT_1_12_1(338, 10, "1.12.1"),
	MINECRAFT_1_12(335, 9, "1.12"),
	MINECRAFT_1_11_1(316, 8, "1.11.2"),
	MINECRAFT_1_11(315, 7, "1.11 - 1.11.1"),
	MINECRAFT_1_10(210, 6, "1.10-  1.10.2"),
	MINECRAFT_1_9_4(110, 5, "1.9.4"),
	MINECRAFT_1_9_2(109, 4, "1.9.2"),
	MINECRAFT_1_9_1(108, 3, "1.9.1"),
	MINECRAFT_1_9(107, 2, "1.9"),
	MINECRAFT_1_8(47, 1,"1.8 - 1.8.9"),
	UNKNOWN(-1, 0, "Not supported!");

	private final int id;
	private final int orderId;
	private final String name;

	/**
	 * Returns the network version id of this protocol version
	 * @return network id of this protocol version
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns user friendly version name <br>
	 * This name can change, so it shouldn't be used as a key anywhere
	 * @return user friendly version name
	 */
	public String getName() {
		return name;
	}

	public boolean isAfter(ServerVersion another) {
		return orderId > another.orderId;
	}

	public boolean isBefore(ServerVersion another) {
		return orderId < another.orderId;
	}

}