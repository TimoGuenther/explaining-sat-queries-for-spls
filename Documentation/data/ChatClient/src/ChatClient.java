public class ChatClient {
	public void onMessageReceived(ChatMessage msg) {
		printMessage(msg);
        // #if MessageNotification
		if (!isMessageRead(msg)) {
			// #if NativeNotification
//@			displayNativeNotification(msg);
			// #endif
			// #if HighlightedTab
			highlightTab(msg);
			// #endif
			// #if UnreadMessageCounter
//@			increaseUnreadMessageCounter(msg);
			// #endif
		}
        // #endif
	}
}
