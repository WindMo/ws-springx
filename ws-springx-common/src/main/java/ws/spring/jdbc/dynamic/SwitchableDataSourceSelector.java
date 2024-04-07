package ws.spring.jdbc.dynamic;

import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;

/**
 * 可切换控制的数据源选择器
 *
 * @author WindShadow
 * @version 2024-03-31.
 */
public interface SwitchableDataSourceSelector extends DataSourceSelector {

   /** 使此数据源选择器选中默认数据源 */
   void useDefaultDataSource();

   /**
    * 使此数据源选择器选中指定数据源
    * @param name 数据源名称，必须是非空的
    */
   void selectedDataSource(@NonNull String name);

   /**
    * 重置此数据源选择器，使其不选中任何数据源
    */
   void resetSelected();

   default boolean isUseDefaultDataSource() {
      return currentDataSource() == null;
   }

   /**
    * 选择默认数据源，以执行特定操作，后重置选择器
    * @param runnable
    */
   void runWithDefault(@NonNull Runnable runnable);

   /**
    * 选择指定数据源，以执行特定操作，后重置选择器
    * @param name 数据源名称，必须是非空的
    * @param runnable
    */
   void runWith(@NonNull String name, @NonNull Runnable runnable);

   /**
    * 选择默认数据源，以执行特定操作，后重置选择器
    * @param callable
    */
   <T> T runWithDefault(@NonNull Callable<T> callable) throws Exception ;

   /**
    * 选择指定数据源，以执行特定操作，后重置选择器
    * @param name 数据源名称，必须是非空的
    * @param callable
    */
   <T> T runWith(@NonNull String name, @NonNull Callable<T> callable) throws Exception;
}
