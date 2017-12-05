/**
 * Created on 2017年11月27日 下午3:03:35
 *
 * Description: [描述该类概要功能介绍]
 *
 * Company:     [尚德机构]
 *
 * @author      [liujiangbo]
 *
*/
package com.sunlands.chainserver;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created on 2017年11月27日 下午3:03:35
 * 
 * Description: [jar包启动服务抽像类,协助责任链模式启动多个服务]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
public abstract class BootJob {
	protected BootJob next;

	protected abstract void start();

	protected abstract void stop();

	// 启动一个服务
	public void startNext() {
		wrapOptional().ifPresent(x -> x.start());
	}

	private Optional<BootJob> wrapOptional() {
		return Optional.ofNullable(next);
	}

	// 关闭一个服务
	public void stopNext() {
		wrapOptional().ifPresent(x -> x.stop());
	}

	public BootJob setNext(BootJob next) {
		this.next = next;
		return next;
	}

	public BootJob setNext(Supplier<BootJob> next, boolean enable) {
		if (enable)
			return setNext(next.get());
		return this;

	}

	protected String getNextName() {
		return next.getName();
	}

	protected String getName() {
		return this.getClass().getSimpleName();
	}

}
