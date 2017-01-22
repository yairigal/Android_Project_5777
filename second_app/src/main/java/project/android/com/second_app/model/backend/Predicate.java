package project.android.com.second_app.model.backend;

/**
 * Created by Yair on 2017-01-18.
 */

public interface Predicate<Input> {
    boolean Do(Input input);
}
