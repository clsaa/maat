package com.clsaa.maat.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 客户端可以动态注册业务码解析器，每次调用都是线程安全的
 *
 * @author 任贵杰
 */
public final class RestResultProviders {

    private static final Logger logger = LoggerFactory.getLogger(RestResultProviders.class);

    private RestResultProviders() {
        throw new UnsupportedOperationException();
    }

    private static final List<RestResultProvider> REST_RESULT_PROVIDERS =
            new CopyOnWriteArrayList<>();

    static {
        // 自动加载业务码解析器
        ServiceLoader<RestResultProvider> providerLoader =
                ServiceLoader.load(RestResultProvider.class);
        for (RestResultProvider aProviderLoader : providerLoader) {
            add(aProviderLoader);
        }
        // 当前路径业务码解码器
        try {
            List<Class<?>> restResultProviders = getAllAssignedClass(RestResultProvider.class);
            for (Class<?> restResultProvider : restResultProviders) {
                add((RestResultProvider) restResultProvider.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态添加RestResult解析器，此方法是线程安全的
     *
     * @author 任贵杰
     */
    public static void add(RestResultProvider provider) {
        if (provider == null) {
            return;
        }
        REST_RESULT_PROVIDERS.add(provider);
    }

    /**
     * 将某个source适配成RestResult(code 和message)，如果source不能解析，则默认返回一个 业务码，code=UNKNOWN，message为source
     * class的名字或者空。
     *
     * @return never return null
     */
    static RestResult getRestResult(Object source) {
        if (source != null && !REST_RESULT_PROVIDERS.isEmpty()) {
            for (RestResultProvider provider : REST_RESULT_PROVIDERS) {
                if (provider.support(source)) {
                    RestResult rr = provider.produce(source);
                    if (rr == null) {
                        continue;
                    }
                    return rr;
                }
            }
        }
        return new StandardRestResult(
                source == null ? "" : source.getClass().getSimpleName());
    }

    /**
     * 获取同一路径下所有子类或接口实现类
     */
    private static List<Class<?>> getAllAssignedClass(Class<?> cls) throws
            ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 取得当前类路径下的所有类
     */
    private static List<Class<?>> getClasses(Class<?> cls) throws ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }


    /**
     * 迭代查找类
     */
    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }
}
