package models

import com.google.inject.AbstractModule
import service.DataService

class DataServiceModule extends AbstractModule {

  def configure() = {
    bind(classOf[DataService]).asEagerSingleton
  }

}
