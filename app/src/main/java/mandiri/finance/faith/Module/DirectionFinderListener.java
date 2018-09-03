package mandiri.finance.faith.Module;

import java.util.List;

/**
 * Created by Gue-PC on 3/20/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
    void onDirectionFinderSearch();
}
