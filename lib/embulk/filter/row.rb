Embulk::JavaPlugin.register_filter(
  "row", "org.embulk.filter.RowFilterPlugin",
  File.expand_path('../../../../classpath', __FILE__))
