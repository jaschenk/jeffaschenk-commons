package jeffaschenk.commons.system.internal.scheduling;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import fr.dyade.jdring.AlarmEntry;
import fr.dyade.jdring.AlarmListener;
import fr.dyade.jdring.AlarmManager;
import fr.dyade.jdring.PastDateException;
import jeffaschenk.commons.system.internal.file.services.ServiceTask;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceStateType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServicesEvent;
import jeffaschenk.commons.touchpoint.model.dao.SystemDAO;
import jeffaschenk.commons.environment.SystemEnvironmentBean;
import jeffaschenk.commons.touchpoint.model.transitory.ParsedAlarmEntry;
import jeffaschenk.commons.touchpoint.model.wrappers.CollectionMapBean;
import jeffaschenk.commons.types.EnvironmentType;
import jeffaschenk.commons.util.StringUtils;
import jeffaschenk.commons.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
     * Task Scheduler
     */
    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * Task Executor
     */
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * JDRING Global Scheduler
     */
    private AlarmManager alarmManager;

    /**
     * Major Running Service Task States.
     */
    private Map<LifeCycleServiceType, LifeCycleServiceTaskState> taskStates = new HashMap<>();

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
     * Provides ability to remove an Action based upon a
     * the name of the Action.
     *
     * @param actionBeanName
     * @return boolean indicating if Action has een removed.
     */
    @SuppressWarnings("unchecked")
    public boolean removeAction(String actionBeanName) {
        // *******************************************
        // Validate Parameters.
        if (StringUtils.isEmpty(actionBeanName)) {
            return false;
        }
        // ************************************************
        // Find our Existing a Alarm Entry for this Action
        List<AlarmEntry> entries = this.alarmManager.getAllAlarms();
        if ((entries == null) || (entries.isEmpty())) {
            return false;
        }
        // Spin through and Find our Entry.
        AlarmEntry entryToBeRemoved = null;
        for (AlarmEntry entry : entries) {
            // TODO FIX --
            //if (entry.getName().equalsIgnoreCase(actionBeanName)) {
            //    entryToBeRemoved = entry;
            //}
        }
        if (entryToBeRemoved == null) {
            return false;
        }
        // ***********************************************
        // Remove Existing a Alarm Entry for this Action
        logger.info("Attempting removal of Alarm Entry:[" + entryToBeRemoved.toString() + "]");
        return this.alarmManager.removeAlarm(entryToBeRemoved);
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
    @Override
	public String resolveCronClassKey(String cronClassKey) {
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


    /**
     * Private Helper Method to parse the String version of
     * the AlarmEntry to display nicely in HTML.
     * <p/>
     * What we will parse and prettify.
     * <p/>
     * Alarm (heartBeat) params minute={0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55}
     * hour={-1} dayOfMonth={-1} month={-1} dayOfWeek={-1}
     * (next alarm date=Sat Jan 28 17:55:00 PST 2012)
     *
     * @param entry
     * @return ParsedAlarmEntry
     */
    private ParsedAlarmEntry parseAlarmEntryString(AlarmEntry entry) {
        if (entry == null) {
            return null;
        }
        String str = entry.toString();
        ParsedAlarmEntry parsedAlarmEntry = new ParsedAlarmEntry();
        // TODO FIX -- parsedAlarmEntry.setName(entry.getName());
        parsedAlarmEntry.setMinute(parseNamedElement(str, "minute={", "}"));
        parsedAlarmEntry.setHour(parseNamedElement(str, "hour={", "}"));
        parsedAlarmEntry.setDayOfMonth(parseNamedElement(str, "dayOfMonth={", "}"));
        parsedAlarmEntry.setMonth(parseNamedElement(str, "month={", "}"));
        parsedAlarmEntry.setDayOfWeek(parseNamedElement(str, "dayOfWeek={", "}"));
        parsedAlarmEntry.setNextAlarm(parseNamedElement(str, "(next alarm date=", ")"));
        // return Parsed Entry for Display
        return parsedAlarmEntry;
    }

    /**
     * Private Helper to Parse String of the AlarmEntry
     *
     * @param str
     * @param beginChars
     * @param endChar
     * @return String
     */
    private String parseNamedElement(String str, String beginChars, String endChar) {
        String content = null;
        int x = str.indexOf(beginChars);
        if (x > 0) {
            content = str.substring(x + beginChars.length());
            int y = content.indexOf(endChar);
            if (y > 0) {
                content = content.substring(0, y);
            }
            if (content.equalsIgnoreCase("-1")) {
                content = "Any";
            }
            // Transform to
            // make the Entry Readable for Months, and Day of Week.
            if (beginChars.startsWith("month")) {
                content = this.transformMonth(content);
            } else if (beginChars.startsWith("dayOfWeek")) {
                content = this.transformDayOfWeek(content);
            }
        }
        return content;
    }

    /**
     * Private helper to transform Cron Month to Human Readable.
     *
     * @param rawData
     * @return String
     */
    private String transformMonth(final String rawData) {
        StringBuffer sb = new StringBuffer();
        for (String element : rawData.split(",")) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            // Transform
            switch (element.trim().toLowerCase()) {
                case "0":
                    sb.append("Jan");
                    break;
                case "1":
                    sb.append("Feb");
                    break;
                case "2":
                    sb.append("Mar");
                    break;
                case "3":
                    sb.append("Apr");
                    break;
                case "4":
                    sb.append("May");
                    break;
                case "5":
                    sb.append("Jun");
                    break;
                case "6":
                    sb.append("Jul");
                    break;
                case "7":
                    sb.append("Aug");
                    break;
                case "8":
                    sb.append("Sep");
                    break;
                case "9":
                    sb.append("Oct");
                    break;
                case "10":
                    sb.append("Nov");
                    break;
                case "11":
                    sb.append("Dec");
                    break;
                default:
                    sb.append(element);
                    break;
            }

        }
        return sb.toString();
    }

    /**
     * Private helper to transform Cron Month to Human Readable.
     *
     * @param rawData
     * @return String
     */
    private String transformDayOfWeek(final String rawData) {
        StringBuffer sb = new StringBuffer();
        for (String element : rawData.split(",")) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            // Transform
            switch (element.trim().toLowerCase()) {
                case "1":
                    sb.append("Sun");
                    break;
                case "2":
                    sb.append("Mon");
                    break;
                case "3":
                    sb.append("Tue");
                    break;
                case "4":
                    sb.append("Wed");
                    break;
                case "5":
                    sb.append("Thu");
                    break;
                case "6":
                    sb.append("Fri");
                    break;
                case "7":
                    sb.append("Sat");
                    break;
                default:
                    sb.append(element);
                    break;
            }

        }
        return sb.toString();
    }

    /**
     * Method Entered When a LifeCycle Services Event gets published.
     * This Method allows our scheduler to consume the Event and provide
     * state information for all Tasks.
     *
     * @param event
     */
    public void onApplicationEvent(LifeCycleServicesEvent event) {
        logger.info("LifeCycle Services Event Consumed: " + event.toString());
        // Check for Update Cleanup immediately...
        if (event.getLifeCycleServiceStateType().equals(LifeCycleServiceStateType.UPDATE_CLEANUP)) {
            logger.warn("Detected Update Clean-Up Event for:" + event.getMessage());
            // **************************************************
            // Process Completed Demographics LifeCycle,
            // Remove the Update Payload elements,
            // as those have now been processed.
            //
            if (event.getLifeCycleServiceType().index()
                    == LifeCycleServiceType.DEMOGRAPHIC_UPDATES_COMPLETED_LIFECYCLE.index()) {
                // ***************************************************
                //
                // Data from Above Event:
                //
                // LifeCycle Services Event Consumed: LifeCycleServicesEvent{lifeCycleServiceType=DEMOGRAPHIC_UPDATES_COMPLETED_LIFECYCLE,
                // lifeCycleServiceStateType=UPDATE_CLEANUP,
                // timestamp_when_event_published=1326175084976,
                // message='Demographics Alt_ID:[10152100] has a completed all existing Lifecycle Updates',
                // payload=[alt_demographics_profile_update_id{alt_id=10152100, modification_timestamp=12272011 15:25:55},
                //          alt_demographics_profile_update_id{alt_id=10152100, modification_timestamp=12272011 15:26:49},
                //          alt_demographics_profile_update_id{alt_id=10152100, modification_timestamp=12272011 21:10:42}]}
                // com.tmatrix.wga.services.scheduler.events.LifeCycleServicesEvent[
                //  source=com.tmatrix.wga.services.extract.ExtractLifecycleUpdateDeterminationForDemographics@34a012a6]
                //
                // Iterate Over Each Object Key and Remove From the database.
                //for (Object update_id : event.getPayload()) {
                //    this.entityDAO.removeDemographicUpdate((alt_demographics_profile_update_id) update_id);
                //}

            } // End of Demographic Update LifeCycle

            //
            // Add Additional Update Closure Events here if needed...
            //
        }

        // Do we have an Existing Task State?
        if (this.taskStates.containsKey(event.getLifeCycleServiceType())) {
            LifeCycleServiceTaskState lifeCycleServiceTaskState =
                    this.taskStates.get(event.getLifeCycleServiceType());
            // Is the Task Done?
            if (event.getLifeCycleServiceStateType().equals(LifeCycleServiceStateType.DONE)) {
                lifeCycleServiceTaskState.setCurrentLifeCycleServiceState(LifeCycleServiceStateType.DONE);
                lifeCycleServiceTaskState.setDone(TimeUtils.now());
            } else if (event.getLifeCycleServiceStateType().equals(LifeCycleServiceStateType.FAILURE)) {
                logger.warn("Detected Failure Event!");
                lifeCycleServiceTaskState.setCurrentLifeCycleServiceState(LifeCycleServiceStateType.DONE);
                lifeCycleServiceTaskState.setDone(TimeUtils.now());
            }
        }
        return;
    }

    /**
     * Schedule Runnable Wrapper Interface
     *
     * @param runnableTask
     * @param scheduledTask
     */
    public synchronized boolean scheduleTask(ServiceTask runnableTask, Date scheduledTask) {
        // Determine if this task Type can Run at this time.
        if (isTaskRunnable(runnableTask.getLifeCycleServiceType())) {
            // Add Task to Task States.
            LifeCycleServiceTaskState lifeCycleServiceTaskState =
                    new LifeCycleServiceTaskState(runnableTask.getLifeCycleServiceType());
            lifeCycleServiceTaskState.setCurrentLifeCycleServiceState(LifeCycleServiceStateType.BEGIN);
            lifeCycleServiceTaskState.setStarted(TimeUtils.now());
            lifeCycleServiceTaskState.setDone(0);
            taskStates.put(lifeCycleServiceTaskState.getLifeCycleServiceType(), lifeCycleServiceTaskState);
            // Schedule Task
            taskScheduler.schedule(runnableTask, scheduledTask);
            logger.warn("Scheduled Task:[" + runnableTask.getLifeCycleServiceType().text() +
                    "] to be Executed at:[" + scheduledTask.toString() + "].");
            return true;
        }
        return false;
    }

    /**
     * Private Helper Method to determine if the Task is Runnable or not.
     *
     * @param lifeCycleServiceType
     * @return boolean Indicator if Task can run or not.
     */
    private boolean isTaskRunnable(LifeCycleServiceType lifeCycleServiceType) {
        // Determine if we can run the requested Task at this time or not?
        if (this.taskStates.containsKey(lifeCycleServiceType)) {
            LifeCycleServiceTaskState lifeCycleServiceTaskState = this.taskStates.get(lifeCycleServiceType);
            if ((!lifeCycleServiceTaskState.getCurrentLifeCycleServiceState().equals(LifeCycleServiceStateType.DONE)) ||
                    (lifeCycleServiceTaskState.getDone() <= 0)) {
                logger.warn("Unable to Schedule Task:[" + lifeCycleServiceType.text() +
                        "], since task is currently running or has been scheduled.");
                return false;
            }
        }
        // Allow running Task Type.
        return true;
    }


    /**
     * Schedule Runnable Wrapper Interface
     *
     * @return String providing textual data representing active Thread count.
     */
    @Override
    public int getActiveThreadPoolTasks() {
        return taskExecutor.getActiveCount();
    }

    /**
     * Obtain our current schedule
     *
     * @return String - HTML Current Schedule Status.
     */
    @SuppressWarnings("unchecked")
    public String getCurrentSchedule() {
        List<AlarmEntry> alarms = this.alarmManager.getAllAlarms();
        StringBuffer sb = new StringBuffer();
        if ((alarms == null) || (alarms.isEmpty())) {
            sb.append("<br/><b><i>Currently there are no Processes scheduled.</i></b><br/>");
        } else {
            sb.append("<br/><b><i>Current Process Scheduled:</i></b><br/>");
            sb.append("<table border='2'>");
            sb.append("<tr>");
            sb.append("<td>");
            sb.append("<b>Alarm Name</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Next Alarm</b>");
            sb.append("</td>");
            sb.append("</tr>");
            for (AlarmEntry entry : alarms) {
                ParsedAlarmEntry parsedAlarmEntry = this.parseAlarmEntryString(entry);
                sb.append(parsedAlarmEntry.toHTML());
            }
            sb.append("</table>");
        }
        return sb.toString();
    }

    /**
     * Obtain our current schedule
     *
     * @return String - HTML Current Schedule Status.
     */
    @SuppressWarnings("unchecked")
    public String getFullCurrentSchedule() {
        List<AlarmEntry> alarms = this.alarmManager.getAllAlarms();
        StringBuffer sb = new StringBuffer();
        if ((alarms == null) || (alarms.isEmpty())) {
            sb.append("<br/><b><i>Currently there are no Processes scheduled.</i></b><br/>");
        } else {
            sb.append("<br/><b><i>Current Process Scheduled:</i></b><br/>");
            sb.append("<table border='2'>");
            sb.append("<tr>");
            sb.append("<td>");
            sb.append("<b>Alarm Name</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Next Alarm</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Minute</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Hour</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Day of Month</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Month</b>");
            sb.append("</td>");
            sb.append("<td>");
            sb.append("<b>Day of Week</b>");
            sb.append("</td>");
            sb.append("</tr>");
            for (AlarmEntry entry : alarms) {
                ParsedAlarmEntry parsedAlarmEntry = this.parseAlarmEntryString(entry);
                sb.append(parsedAlarmEntry.toFullHTML());
            }
            sb.append("</table>");
        }
        return sb.toString();
    }



}
