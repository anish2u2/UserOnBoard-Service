/**
 * 
 */
package com.onboard.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.onboard.service.service.RolesService;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */

@Component
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent> {
	
	Logger logger=LoggerFactory.getLogger(ContextInitializedListener.class.getName());
	
	@Autowired
	private RolesService service;
	
	/*@EventListener(classes = ContextRefreshedEvent.class)
	public void init() {
		
	}*/

	@Override
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		new Thread(()->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		logger.info("Intializing Roles.");
		service.initializeRoles();
		logger.info("Intializing Roles done.");
		
		}).start();
	}
	
}
