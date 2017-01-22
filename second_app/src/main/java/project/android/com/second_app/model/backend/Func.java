package project.android.com.second_app.model.backend;

/**
 * Created by Yair on 2017-01-18.
 */

public interface Func<Parameter,Result> {
    Result Do(Parameter parm);
}
