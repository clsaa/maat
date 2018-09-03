package com.clsaa.maat.constant.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link MessageState}的上下文
 * 业务真正操作的对象，根据传入的状态，找到对应的{@link AbstractState}实现
 *
 * @author 任贵杰
 */
public class StateContext {

  private static final Logger LOGGER = LoggerFactory.getLogger(StateContext.class);

  /**
   * 判断消息状态是否满足切换条件
   * 对业务暴露的方法，调用持有的state进行真正的操作
   *
   * @param stateFrom 消息原状态
   * @param stateTo   消息目标状态
   * @return 是否可以切换
   */
  public static boolean validateState(String stateFrom, String stateTo) {
    AbstractState state = getStateClass(stateFrom);
    MessageState eStateTo = MessageState.getByCode(stateTo);
    return state.validateState(eStateTo);
  }

  /**
   * 根据消息状态实例化对应的{@link AbstractState}子类对象
   * 消息状态和State对象的映射关系在{@link MessageState}中
   *
   * @param stateFrom 消息原状态
   * @return ? extends {@link AbstractState}
   */
  private static AbstractState getStateClass(String stateFrom) {
    MessageState eStateFrom = MessageState.getByCode(stateFrom);
    Class<? extends AbstractState> clz = eStateFrom.getMappingStateClass();
    AbstractState localState = null;
    try {
      localState = clz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      LOGGER.error("{}初始化失败", clz.getName());
    }
    return localState;
  }
}