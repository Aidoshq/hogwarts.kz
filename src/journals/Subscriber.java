package journals;

import utils.Post;

public interface Subscriber  {
	/**
	 * adds new notifications to subscriber's inbox
	 * @param post new Notification
	 */
	void update(Post post);
}
