package filters


import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

class MyFilters @Inject()(corsFilter: CORSFilter)
  extends DefaultHttpFilters(corsFilter)