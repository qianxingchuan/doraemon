#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};
import io.github.qianxingchuan.framework.doraemon.BundleService;
/**
 * @author xingchuan.qxc
 * @date 2019/5/24 15:59
 */
public class BundleServiceImpl implements BundleService {

    @Override
    public void doIt() {
        // init your bundle here.
    }

    @Override
    public <T> T getBundleBean(Class<T> typeClass) {
        return null;
    }
}
