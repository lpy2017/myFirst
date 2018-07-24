# Default Properties file for use by StdSchedulerFactory
# to create a Quartz Scheduler Instance, if a different
# properties file is not explicitly specified.
#
org.quartz.scheduler.instanceId = AUTO 
org.quartz.scheduler.instanceName= DefaultQuartzScheduler
org.quartz.scheduler.rmi.export= false
org.quartz.scheduler.rmi.proxy= false
org.quartz.scheduler.wrapJobExecutionInUserTransaction= false

org.quartz.threadPool.class= org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount= 25
org.quartz.threadPool.threadPriority= 5
org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread= true
org.quartz.scheduler.batchTriggerAcquisitionMaxCount=50
org.quartz.jobStore.misfireThreshold= 60000

#ram
#org.quartz.jobStore.class: org.quartz.simpl.RAMJobStore
 
# Configure JobStore  
org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
org.quartz.jobStore.tablePrefix = QRTZ_
org.quartz.jobStore.dataSource = myDS

#cluster
org.quartz.jobStore.isClustered = ${isCluster?if_exists }
org.quartz.jobStore.clusterCheckinInterval = 20000 

# Configure Datasources  
org.quartz.dataSource.myDS.driver = com.mysql.jdbc.Driver
org.quartz.dataSource.myDS.URL = jdbc:mysql:// ${dbip?if_exists}:${dbport?if_exists}/${dbname?if_exists}?characterEncoding=utf-8
org.quartz.dataSource.myDS.user = ${dbuser?if_exists}
org.quartz.dataSource.myDS.password = ${dbpassword?if_exists}
org.quartz.dataSource.myDS.maxConnections = 50
