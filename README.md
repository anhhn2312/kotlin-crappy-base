Coding Guide
============

Build configurations
--------------------
[build_dependencies/project-config.gradle][1]:
This file contains the app config (packageName, versionName, versionCode, min and compile and target SDK), signing configs and other build variant fields value.

[build_dependencies/project-dependencies.gradle][2]:
This is where all dependencies for the whole app are declared. They are classified into categories that are represented by arrays of strings.
This makes the dependencies and your build.gradle look well-ordered and easy to track and manage.

App Module
----------

Steps for creating an Activity:
1. Create Activity class, extends [BaseActivity][3] class.
2. Create xml layout file and assign R.layout.activity_your_layout to layoutRes() method in Activity class.
3. Declare your activity in [ActivityBindingModule][4] class (for Dagger's sake).
4. Done.

Steps for creating a Fragment:
1. Create layout file fragment_name.xml
2. Add data and variable tag if you are about to user data binding. Otherwise, skip this.
3. Create Fragment class, extends [BaseFragment<FragmentNameBinding>][5] if you are using data binding. If not, extends BaseFragment<*>.
4. Declare your fragment in [FragmentBindingModule][6] class.
5. Done.

Steps for creating a ViewModel:
1. Create ViewModel class and extends [BaseViewModel][7], include Repository or anything you want in @Inject constructor(...).
2. Declare your ViewModel class in [ViewModelModule][8] class.
3. Done.

Steps for creating a Worker (to implement WorkManager)
1. Create a Worker class extends [RxWorker][9].
2. Include anything you want in @Inject constructor(...).
3. You should write enqueue() method in companion object. See [DummyWorker][13] sample class for details.
4. Declare your Worker class in [WorkersModule][10] class.
5. Done.


Data Module
-----------

Steps for creating a [RepositoryProvider][14]:
1. Declare a repository interface.
2. Create RepositoryProvider class and implements the previous created interface.
3. Declare your RepositoryProvider in RepositoryModule.
4. Done

Steps for creating a Dao:
1. Create your Dao class, extends [BaseDao<YourModel>][11]. Make sure that you make YourModel a Room entity.
2. Include YourModel class in entities array in [AppDatabase][12] class.
3. Define your Dao getter method in [AppDatabase][12] class.
4. Increase the Database version if needed.
5. Done.

Domain Module
-------------
Declare your API service, request and response classes here.

Extensions Module
-----------------
Include Kotlin extension methods here.

Resources Module
----------------
Contains all resources and dependencies for the whole project.


[1]: /build_dependencies/project-config.gradle
[2]: /build_dependencies/project-dependencies.gradle
[3]: /app/src/main/java/com/dinominator/kotlin_awesome_app/base/BaseActivity.kt
[4]: /app/src/main/java/com/dinominator/kotlin_awesome_app/di/modules/ActivityBindingModule.kt
[5]: /app/src/main/java/com/dinominator/kotlin_awesome_app/base/BaseFragment.kt
[6]: /app/src/main/java/com/dinominator/kotlin_awesome_app/di/modules/FragmentBindingModule.kt
[7]: /app/src/main/java/com/dinominator/kotlin_awesome_app/base/BaseViewModel.kt
[8]: /app/src/main/java/com/dinominator/kotlin_awesome_app/di/modules/ViewModelModule.kt
[9]: https://developer.android.com/reference/androidx/work/RxWorker
[10]: /app/src/main/java/com/dinominator/kotlin_awesome_app/di/modules/WorkersModule.kt
[11]: /data/src/main/java/com/dinominator/data/persistence/dao/BaseDao.kt
[12]: /data/src/main/java/com/dinominator/data/persistence/db/AppDatabase.kt
[13]: /app/src/main/java/com/dinominator/kotlin_awesome_app/platform/works/DummyWorker.kt
[14]: /data/src/main/java/com/dinominator/data/repository