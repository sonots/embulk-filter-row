Embulk::JavaPlugin.register_filter(
  "row", "org.embulk.filter.row.RowFilterPlugin",
  File.expand_path('../../../../classpath', __FILE__))
