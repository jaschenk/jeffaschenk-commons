package jeffaschenk.commons.system.internal.scheduling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import fr.dyade.jdring.AlarmEntry;
import fr.dyade.jdring.AlarmListener;
import fr.dyade.jdring.AlarmManager;
import fr.dyade.jdring.PastDateException;
import jeffaschenk.commons.touchpoint.model.dao.SystemDAO;
import jeffaschenk.commons.environment.SystemEnvironmentBean;
import jeffaschenk.commons.touchpoint.model.wrappers.CollectionMapBean;
import jeffaschenk.commons.types.EnvironmentType;
import jeffaschenk.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


/**
 * Content Management Service Interface Provides Access to resolve any
 * dynamic content directly from an established cache or
 * 
 * @author jeffaschenk@gmail.com
 */
@Service("localSchedulingService")
public class LocalSchedulingServiceImpl implements LocalSchedulingService,
		ApplicationContextAware {

	/**
	 * Logging
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(LocalSchedulingServiceImpl.class);

	/**
	 * Globals
	 */
	private final static String CRON_ELEMENT_SEPARATORS = "-,/";
	private final static String AUCTION = "AUCTION";

	/**
	 * Initialization Indicator.
	 */
	private boolean initialized = false;

	/**
	 * Injected Common System Environment Property to check to see if this
	 * facility is enabled or not.
	 */
	@Value("#{systemEnvironmentProperties['internal.scheduling.tasks.enabled']}")
	private boolean serviceEnabled;

	/**
	 * Injected Common System Environment Property to configure the CRON. prefix
	 * for specified system Environment Variables.
	 */
	@Value("#{systemEnvironmentProperties['internal.scheduling.tasks.cron.prefix']}")
	private String CRON_SEARCH_KEY;

	/**
	 * Global Services Injected
	 */
	@Autowired
	protected SystemDAO systemDAO;

	@Resource(name = "cronKeyToBeanMap")
	private CollectionMapBean cronKeyToBeanMap;

	@Autowired
	@Qualifier("systemEnvironmentPropertyAccessor")
	private SystemEnvironmentBean systemEnvironmentBean;

	/**
	 * JDRING Global Scheduler
	 */
	private AlarmManager alarmManager;

	/**
	 * Spring Application Context, used to obtain access to Resources on
	 * Classpath.
	 */
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Initialize the Content Management System Interface
	 */
	@PostConstruct
	public void initialize() {
		// ************************************************************
		// Ensure we have our System DAO Available.
		if (this.systemDAO == null) {
			logger
					.error("Application Context Initialization is suspect, as one or more required Services are not available at this time, Disabling Service.");
			this.initialized = false;
			return;
		}
		// *********************************************************************
		// Ensure Service Enabled and The Runtime Environment is a JVM.
		// We will not initialize our Internal Scheduler if our Environment Type
		// is not "JVM", however, for "ESB", we will auto-initialize and
		// filter what types of Alarm/Events will be started.
		//
		if ((this.serviceEnabled)
				&& (this.systemEnvironmentBean.runningEnvironmentType() == EnvironmentType.JVM)) {
			logger
					.warn("Activating Local Scheduling Service Provider Interface.");

			// *************************************************************
			// Construct our Alarm Manager.
			this.alarmManager = new AlarmManager(true,
					"SOR_INTERNAL_AlarmManager");
			// *************************************************************
			// Show our Cron Class Key to ClassName Map.
			if ((cronKeyToBeanMap == null)
					|| (cronKeyToBeanMap.getMap().isEmpty())) {
				logger
						.error("Unable to Access Cron Key to Bean Name, Disabling Service.");
				this.initialized = false;
				return;
			}
			for (String key : cronKeyToBeanMap.getMap().keySet()) {
				logger.warn("  * CRON Key:[" + key
						+ "], References Bean Name:["
						+ cronKeyToBeanMap.getMap().get(key) + "]");
			}
			// *************************************************************
			// Now use System Services to Obtain the SysEnvironment
			// CRON Keys, spin through and activate all Triggers
			this.initializeSchedulerFromPersistentStore(CRON_SEARCH_KEY, null);
			// *************************************************************
			// Show Summary of Schedule
			List list = this.alarmManager.getAllAlarms();
			for (Iterator it = list.iterator(); it.hasNext();) {
				logger.warn("Scheduled Alarm:[" + it.next() + "]");
			}
			// initialized
			this.initialized = true;
		} else if (this.systemEnvironmentBean.runningEnvironmentType() == EnvironmentType.ESB) {
			logger
					.warn("Activating Local Scheduling Service Provider Interface with Filtered Alarm Definitions for Environment:["
							+ this.systemEnvironmentBean
									.runningEnvironmentType().name() + "]");
			// *************************************************************
			// Construct our Alarm Manager.
			this.alarmManager = new AlarmManager(true,
					"SOR_INTERNAL_ESB_AlarmManager");
			// *************************************************************
			// Show our Cron Class Key to ClassName Map.
			if ((cronKeyToBeanMap == null)
					|| (cronKeyToBeanMap.getMap().isEmpty())) {
				logger
						.error("Unable to Access Cron Key to Bean Name, Disabling Service.");
				this.initialized = false;
				return;
			}
			for (String key : cronKeyToBeanMap.getMap().keySet()) {
				if (key.toUpperCase().contains(AUCTION)) {
					continue;
				}
				logger.warn("  * CRON Key:[" + key
						+ "], References Bean Name:["
						+ cronKeyToBeanMap.getMap().get(key) + "]");
			}
			// *************************************************************
			// Now use System Services to Obtain the SysEnvironment
			// CRON Keys, spin through and activate all Triggers
			this.initializeSchedulerFromPersistentStore(CRON_SEARCH_KEY,
					AUCTION);
			// *************************************************************
			// Show Summary of Schedule
			List list = this.alarmManager.getAllAlarms();
			for (Iterator it = list.iterator(); it.hasNext();) {
				logger.warn("Scheduled Alarm:[" + it.next() + "]");
			}
			// initialized
			this.initialized = true;
		} else {
			logger
					.warn("Local Scheduling Provider Interface has been Disabled.");
			this.initialized = false;
		}
	}

	/**
	 * Destroy Service Invoked during Termination of the Spring Container.
	 */
	@PreDestroy
	public void destroy() {
		if (this.initialized) {
			logger
					.warn("Deactivating Local Scheduling Service Provider Interface");
			if (this.alarmManager != null) {
				this.alarmManager.removeAllAlarms();
                this.alarmManager.finalize();
			}
			// ***************************
			// Indicate not initialized!
			initialized = false;
			logger
					.warn("Deactivation of Local Scheduling Provider Interface was Successful.");
		}
	}

	/**
	 * Schedule any Necessary Actions Obtained Directly from SysEnvironment
	 * 
	 * @param actionBeanName
	 *            of Internally Scheduler Action.
	 * @param cronExpression
	 * @return boolean
	 */
	@Override
	public boolean scheduleAction(String actionBeanName, String cronExpression) {
		// ******************************************
		// Can we schedule an Action?
		if (!this.serviceEnabled) {
			return false;
		}
		// ******************************************
		// Check against Internal Gate

		// TODO Track Job Initialization to inhibit lots of activity.

		// *******************************************
		// Validate Parameters.
		if (StringUtils.isEmpty(actionBeanName)) {
			return false;
		}
		if (StringUtils.isEmpty(cronExpression)) {
			return false;
		}
		// ********************************************
		// Initialize a Alarm Entry for this Action
		try {
			AlarmEntry alarmEntry = this.parseCronExpression(actionBeanName,
					cronExpression, this
							.obtainComponentReference(actionBeanName));
			if (alarmEntry != null) {
				this.alarmManager.addAlarm(alarmEntry);
				return true;
			}
		} catch (PastDateException pde) {
			logger.error(
					"Unable to Instantiate Scheduled Action Class Exception: "
							+ pde.getMessage(), pde);
		}
		return false;
	}

	/**
	 * Private helper method to Obtain Component Reference of bean Class
	 * 
	 * @param actionBeanName
	 *            of Object to return
	 * @return Object of Instantiated Class or Null.
	 */
	private AlarmListener obtainComponentReference(String actionBeanName) {
		AlarmListener alarmListener = (AlarmListener) applicationContext
				.getBean(actionBeanName);
		if (alarmListener == null) {
			logger
					.error("Unable to obtain Component Action Alarm Listener Reference!");
		}
		// *************************************
		// Return Instantiated Object or Null.
		return alarmListener;
	}

	/**
	 * Private Helper Method to establish Stored CRON Entries to our Scheduler.
	 * 
	 * @param key
	 */
	private void initializeSchedulerFromPersistentStore(String key,
			String excludeFilter) {
		// *************************************************************
		// Now use System Services to Obtain the SysEnvironment
		// CRON Keys, spin through and activate all Triggers
		Map<String, String> cronEntriesFromDB = this.systemDAO
				.findSysEnvironmentProperty(key + "%", null);
		if ((cronEntriesFromDB.size() > 0)) {
			for (String cronKey : cronEntriesFromDB.keySet()) {
				String cronValue = cronEntriesFromDB.get(cronKey);
				if (StringUtils.isEmpty(cronValue)) {
					continue;
				}
				// Filter and Exclude anything containing a piece of the named
				// filter.
				if ((excludeFilter != null)
						&& (cronKey.toUpperCase().contains(excludeFilter))) {
					continue;
				}
				// Establish the Cron Task in our Container Scheduler.
				String actionBeanName = resolveCronClassKey(cronKey
						.substring(key.length()));
				// Now Initialize the Job and Introduce into our Schedule.
				this.scheduleAction(actionBeanName, cronValue);
			}
		} else {
			logger
					.warn("No CRONTAB Entries found in this Instance Store for use, using Cron Tab Key:["
							+ key + "]");
		}
	}

	/**
	 * Simple Private Helper Method to resolve the specific class per the Class
	 * Key Name.
	 * 
	 * @param cronClassKey
	 * @return String
	 */
	private String resolveCronClassKey(String cronClassKey) {
		if (cronKeyToBeanMap.getMap().get(cronClassKey.trim().toUpperCase()) == null) {
			logger.warn("No Associated Class found with CRON Key:["
					+ cronClassKey + "], check your configuration!");
			return null;
		}
		return cronKeyToBeanMap.getMap().get(cronClassKey.toUpperCase()).toString();
	}

	/**
	 * Parse the cronExpress and produce an AlarmEntry for the Alarm Manager to
	 * use for scheduling.
	 * <p/>
	 * CronExpression Examples:
	 * <p/>
	 * Cron Example Patterns:
	 * <p/>
	 * S m h d M DOW 0 0 * * * * = the top of every hour of every day. 0/10 * *
	 * * * * = every ten seconds. ** Will be Ignored! 0 0 8-10 * * * = 8, 9 and
	 * 10 o'clock of every day. 0 0/30 8-10 * * * = 8:00, 8:30, 9:00, 9:30 and
	 * 10 o'clock every day. 0 0 9-17 * * MON-FRI = on the hour nine-to-five
	 * weekdays 0 0 0 25 12 ? = every Christmas Day at midnight
	 * <p/>
	 * Now what is actually down to parse is to ignore the seconds completely,
	 * if seconds are specified, we will ignore for now at least. But the intent
	 * of the internal scheduler is to Alarm at certain times and perform a
	 * task.
	 * <p/>
	 *** Any task that is needed to every every x number of seconds, needs to
	 * run in its own thread not here.
	 * 
	 * @param cronExpression
	 * @param alarmListener
	 *            - Instantiated Alarm Listener for the produced Alarm Entry.
	 * @return AlarmEntry
	 */
	private AlarmEntry parseCronExpression(String name, String cronExpression,
			AlarmListener alarmListener) {
		// ***************************************
		// Initialize
		AlarmEntry alarmEntry;
		int _year = -1;
		// ***************************************
		// Validate
		if ((StringUtils.isEmpty(name))
				|| (StringUtils.isEmpty(cronExpression))
				|| (alarmListener == null)) {
			logger
					.error("Unable to Instantiate new Alarm Entry, one or more method parameters invalid!");
			return null;
		}

		// ***************************************
		// Parse String
		String[] cronArray = cronExpression.split(" ");
		if (cronArray.length < 6) {
			logger.error("Cron Expression Invalid:[" + cronExpression
					+ "], needs at least 6 parameters!");
			return null;
		}
		logger.warn("Parsed Cron Expression from Persistent Store:["
				+ "seconds:[" + cronArray[0] + "], " + "minutes:["
				+ cronArray[1] + "], " + "hours:[" + cronArray[2] + "], "
				+ "dayOfMonth:[" + cronArray[3] + "], " + "month:["
				+ cronArray[4] + "], " + "dayOfWeek:[" + cronArray[5] + "]]");
		// ***************************************
		// Formulate each Alarm Component.
		int x = 0;
		int[][] formulatedCronElementArrays = new int[6][];
		for (String cronElement : cronArray) {
			// Now Parse each element grouping.
			List<Integer> ticList = new ArrayList<Integer>();
			StringTokenizer st = new StringTokenizer(cronElement.trim(),
					CRON_ELEMENT_SEPARATORS, true);
			String lastTick = null;
			String lastSeparator = null;

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if ((token == null) || (token.trim().length() <= 0)) {
					continue;
				}
				if (StringUtils.isNumeric(token)) {
					if (StringUtils.isEmpty(lastSeparator)) {
						try {
							ticList.add(new Integer(token));
						} catch (NumberFormatException nfe) {
							logger
									.error("Invalid Cron Element should have been a numeric value, but was:["
											+ token + "], Ignoring Alarm!");
							return null;
						}
					} else if (lastSeparator.equalsIgnoreCase(",")) {
						try {
							ticList.add(new Integer(token));
						} catch (NumberFormatException nfe) {
							logger
									.error("Invalid Cron Element should have been a numeric value, but was:["
											+ token + "], Ignoring Alarm!");
							return null;
						}
					} else if (lastSeparator.equalsIgnoreCase("-")) {
						try {
							int base = Integer.valueOf((StringUtils
									.isEmpty(lastTick) ? "0" : lastTick));
							int last = Integer.valueOf(token);
							if (base > last) {
								logger
										.error("Invalid Cron Element using dashed sequence, base greater than last:["
												+ token + "], Ignoring Alarm!");
								return null;
							} else if (base < last) {
								for (int i = base + 1; i <= last; i++) {
									ticList.add(new Integer(i));
								}
							} else {
								ticList.add(new Integer(token));
							}
						} catch (NumberFormatException nfe) {
							logger
									.error("Invalid Cron Element should have been a numeric value, but was:["
											+ token + "], Ignoring Alarm!");
							return null;
						}
					} else if (lastSeparator.equalsIgnoreCase("/")) {
						// Only use this notation for seconds / minutes or hours
						try {
							int base = Integer.valueOf((StringUtils
									.isEmpty(lastTick) ? "0" : lastTick));
							int increment = Integer.valueOf(token);
							int end = 60;
							if (x > 2) {
								logger
										.error("Invalid Cron Element unable to use '/' notation for specified parameter:["
												+ token + "], Ignoring Alarm!");
								return null;
							}
							if (x == 2) {
								end = 23;
							}
							for (int i = base + increment; i < end; i = i
									+ increment) {
								ticList.add(new Integer(i));
							}
						} catch (NumberFormatException nfe) {
							logger
									.error("Invalid Cron Element should have been a numeric value, but was:["
											+ token + "], Ignoring Alarm!");
							return null;
						}
					}
					lastSeparator = null;
					lastTick = token;
				} else if (CRON_ELEMENT_SEPARATORS.indexOf(token) > -1) {
					if (StringUtils.isEmpty(lastSeparator)) {
						lastSeparator = token; // Set up to trigger usage.
					} else {
						logger
								.error("Invalid Cron Element back to back Separator Values found, Previous:["
										+ lastSeparator
										+ ", This:["
										+ token
										+ "], Ignoring Alarm!");
						return null;
					}
				} else if ((token.equalsIgnoreCase("*"))
						|| (token.equalsIgnoreCase("?"))) {
					ticList.add(new Integer(-1));
				} else {
					logger
							.error("Invalid Cron Element Unidentifiable Character/Phrase Found:["
									+ token + "], Ignoring Alarm!");
					return null;
				}
			}
			// now place the produced Element Array into a the formulated Array
			// to produce the Alarm Entry.
			formulatedCronElementArrays[x] = new int[ticList.size()];
			int i = -1;
			for (Integer tic : ticList) {
				i++;
				formulatedCronElementArrays[x][i] = tic.intValue();
			}
			x++;
		} // End of For Each Loop.

		try {
			/**
            alarmEntry = new AlarmEntry(name, formulatedCronElementArrays[1],
					formulatedCronElementArrays[2],
					formulatedCronElementArrays[3],
					formulatedCronElementArrays[4],
					formulatedCronElementArrays[5], _year, alarmListener);
            **/
            alarmEntry = new AlarmEntry(formulatedCronElementArrays[1][0],
                    formulatedCronElementArrays[2][0],
                    formulatedCronElementArrays[3][0],
                    formulatedCronElementArrays[4][0],
                    formulatedCronElementArrays[5][0], _year, alarmListener);

			logger.warn("Created Alarm Entry:[" + alarmEntry.toString() + "]");

			// **********************************
			// return New alarm Entry based upon
			// Cron Expression and other
			// default values.
			return alarmEntry;
		} catch (PastDateException pde) {
			logger
					.error("Can not initialize for a Date in the Past, Ignoring Cron Expression:["
							+ cronExpression + "]");
			return null;
		}

	}

}
