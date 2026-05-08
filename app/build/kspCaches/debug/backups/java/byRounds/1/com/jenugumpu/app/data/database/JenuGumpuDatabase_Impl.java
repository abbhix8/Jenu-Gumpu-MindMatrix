package com.jenugumpu.app.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.jenugumpu.app.data.dao.BatchDao;
import com.jenugumpu.app.data.dao.BatchDao_Impl;
import com.jenugumpu.app.data.dao.HarvestLogDao;
import com.jenugumpu.app.data.dao.HarvestLogDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class JenuGumpuDatabase_Impl extends JenuGumpuDatabase {
  private volatile HarvestLogDao _harvestLogDao;

  private volatile BatchDao _batchDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `harvest_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `quantity` REAL NOT NULL, `floralSource` TEXT NOT NULL, `grade` TEXT NOT NULL, `location` TEXT NOT NULL, `date` INTEGER NOT NULL, `notes` TEXT NOT NULL, `isUsedInBatch` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `batches` (`batchId` TEXT NOT NULL, `totalQuantity` REAL NOT NULL, `grade` TEXT NOT NULL, `floralSource` TEXT NOT NULL, `createdDate` INTEGER NOT NULL, `status` TEXT NOT NULL, `qrCodePath` TEXT NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`batchId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `batch_log_cross_ref` (`batchId` TEXT NOT NULL, `harvestLogId` INTEGER NOT NULL, PRIMARY KEY(`batchId`, `harvestLogId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6fafa4111edae2cc73f14657b833545f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `harvest_logs`");
        db.execSQL("DROP TABLE IF EXISTS `batches`");
        db.execSQL("DROP TABLE IF EXISTS `batch_log_cross_ref`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsHarvestLogs = new HashMap<String, TableInfo.Column>(8);
        _columnsHarvestLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("floralSource", new TableInfo.Column("floralSource", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("grade", new TableInfo.Column("grade", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHarvestLogs.put("isUsedInBatch", new TableInfo.Column("isUsedInBatch", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHarvestLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHarvestLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHarvestLogs = new TableInfo("harvest_logs", _columnsHarvestLogs, _foreignKeysHarvestLogs, _indicesHarvestLogs);
        final TableInfo _existingHarvestLogs = TableInfo.read(db, "harvest_logs");
        if (!_infoHarvestLogs.equals(_existingHarvestLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "harvest_logs(com.jenugumpu.app.data.entity.HarvestLog).\n"
                  + " Expected:\n" + _infoHarvestLogs + "\n"
                  + " Found:\n" + _existingHarvestLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsBatches = new HashMap<String, TableInfo.Column>(8);
        _columnsBatches.put("batchId", new TableInfo.Column("batchId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("totalQuantity", new TableInfo.Column("totalQuantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("grade", new TableInfo.Column("grade", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("floralSource", new TableInfo.Column("floralSource", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("qrCodePath", new TableInfo.Column("qrCodePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBatches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBatches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBatches = new TableInfo("batches", _columnsBatches, _foreignKeysBatches, _indicesBatches);
        final TableInfo _existingBatches = TableInfo.read(db, "batches");
        if (!_infoBatches.equals(_existingBatches)) {
          return new RoomOpenHelper.ValidationResult(false, "batches(com.jenugumpu.app.data.entity.Batch).\n"
                  + " Expected:\n" + _infoBatches + "\n"
                  + " Found:\n" + _existingBatches);
        }
        final HashMap<String, TableInfo.Column> _columnsBatchLogCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsBatchLogCrossRef.put("batchId", new TableInfo.Column("batchId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatchLogCrossRef.put("harvestLogId", new TableInfo.Column("harvestLogId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBatchLogCrossRef = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBatchLogCrossRef = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBatchLogCrossRef = new TableInfo("batch_log_cross_ref", _columnsBatchLogCrossRef, _foreignKeysBatchLogCrossRef, _indicesBatchLogCrossRef);
        final TableInfo _existingBatchLogCrossRef = TableInfo.read(db, "batch_log_cross_ref");
        if (!_infoBatchLogCrossRef.equals(_existingBatchLogCrossRef)) {
          return new RoomOpenHelper.ValidationResult(false, "batch_log_cross_ref(com.jenugumpu.app.data.entity.BatchLogCrossRef).\n"
                  + " Expected:\n" + _infoBatchLogCrossRef + "\n"
                  + " Found:\n" + _existingBatchLogCrossRef);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6fafa4111edae2cc73f14657b833545f", "5fa4f677c5022535a59ca8ba62d89954");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "harvest_logs","batches","batch_log_cross_ref");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `harvest_logs`");
      _db.execSQL("DELETE FROM `batches`");
      _db.execSQL("DELETE FROM `batch_log_cross_ref`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(HarvestLogDao.class, HarvestLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BatchDao.class, BatchDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public HarvestLogDao harvestLogDao() {
    if (_harvestLogDao != null) {
      return _harvestLogDao;
    } else {
      synchronized(this) {
        if(_harvestLogDao == null) {
          _harvestLogDao = new HarvestLogDao_Impl(this);
        }
        return _harvestLogDao;
      }
    }
  }

  @Override
  public BatchDao batchDao() {
    if (_batchDao != null) {
      return _batchDao;
    } else {
      synchronized(this) {
        if(_batchDao == null) {
          _batchDao = new BatchDao_Impl(this);
        }
        return _batchDao;
      }
    }
  }
}
