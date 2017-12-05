/**
 * Created on 2017年11月27日 下午3:54:21
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017年11月27日 下午3:54:21
 * 
 * Description: [描述该类概要功能介绍]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
public class BootChain {
	Logger logger = LoggerFactory.getLogger(BootChain.class);

	private static Optional<BootChain> chainOptional = Optional.empty();

	private final BootJob boot = new BootJob() {

		@Override
		protected void start() {
			logger.info("bootchain starting...");
			startNext();
		}

		@Override
		protected void stop() {
			stopNext();
			logger.info("bootchain stopped");
		}
	};

	private BootJob last = boot;

	public void start() {
		boot.start();
	}

	public void stop() {
		boot.stop();
	}

	public static Optional<BootChain> chain() {
		if (!chainOptional.isPresent()) {
			chainOptional = Optional.ofNullable(new BootChain());
		}
		return chainOptional;
	}

	public BootChain boot() {
		return this;
	}

	public BootChain setNext(BootJob bootJob) {
		this.last = last.setNext(bootJob);
		return this;
	}

	public BootChain setNext(Supplier<BootJob> next, boolean enable) {
		if (enable)
			return setNext(next.get());
		return this;
	}

	public void end() {
		setNext(new BootJob() {

			@Override
			protected void stop() {
				logger.info("-----------bootchain stopped all servers-----------------");

			}

			@Override
			protected void start() {
				logger.info("-----congretulations-----bootchain started all servers--------------------");
			}
		});
	}
}
