package menu;

import java.io.IOException;

import users.User;

public interface MenuItem {
	void execute(User u) throws Exception;
}
