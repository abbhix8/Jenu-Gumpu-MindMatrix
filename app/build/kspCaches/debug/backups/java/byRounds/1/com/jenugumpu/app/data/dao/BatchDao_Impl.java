package com.jenugumpu.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jenugumpu.app.data.database.Converters;
import com.jenugumpu.app.data.entity.Batch;
import com.jenugumpu.app.data.entity.BatchLogCrossRef;
import com.jenugumpu.app.data.entity.BatchStatus;
import com.jenugumpu.app.data.entity.BatchWithLogs;
import com.jenugumpu.app.data.entity.FloralSource;
import com.jenugumpu.app.data.entity.HarvestLog;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BatchDao_Impl implements BatchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Batch> __insertionAdapterOfBatch;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<BatchLogCrossRef> __insertionAdapterOfBatchLogCrossRef;

  private final EntityDeletionOrUpdateAdapter<Batch> __deletionAdapterOfBatch;

  private final EntityDeletionOrUpdateAdapter<Batch> __updateAdapterOfBatch;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBatchStatus;

  public BatchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBatch = new EntityInsertionAdapter<Batch>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `batches` (`batchId`,`totalQuantity`,`grade`,`floralSource`,`createdDate`,`status`,`qrCodePath`,`notes`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Batch entity) {
        statement.bindString(1, entity.getBatchId());
        statement.bindDouble(2, entity.getTotalQuantity());
        statement.bindString(3, entity.getGrade());
        final String _tmp = __converters.fromFloralSource(entity.getFloralSource());
        statement.bindString(4, _tmp);
        statement.bindLong(5, entity.getCreatedDate());
        final String _tmp_1 = __converters.fromBatchStatus(entity.getStatus());
        statement.bindString(6, _tmp_1);
        statement.bindString(7, entity.getQrCodePath());
        statement.bindString(8, entity.getNotes());
      }
    };
    this.__insertionAdapterOfBatchLogCrossRef = new EntityInsertionAdapter<BatchLogCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `batch_log_cross_ref` (`batchId`,`harvestLogId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BatchLogCrossRef entity) {
        statement.bindString(1, entity.getBatchId());
        statement.bindLong(2, entity.getHarvestLogId());
      }
    };
    this.__deletionAdapterOfBatch = new EntityDeletionOrUpdateAdapter<Batch>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `batches` WHERE `batchId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Batch entity) {
        statement.bindString(1, entity.getBatchId());
      }
    };
    this.__updateAdapterOfBatch = new EntityDeletionOrUpdateAdapter<Batch>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `batches` SET `batchId` = ?,`totalQuantity` = ?,`grade` = ?,`floralSource` = ?,`createdDate` = ?,`status` = ?,`qrCodePath` = ?,`notes` = ? WHERE `batchId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Batch entity) {
        statement.bindString(1, entity.getBatchId());
        statement.bindDouble(2, entity.getTotalQuantity());
        statement.bindString(3, entity.getGrade());
        final String _tmp = __converters.fromFloralSource(entity.getFloralSource());
        statement.bindString(4, _tmp);
        statement.bindLong(5, entity.getCreatedDate());
        final String _tmp_1 = __converters.fromBatchStatus(entity.getStatus());
        statement.bindString(6, _tmp_1);
        statement.bindString(7, entity.getQrCodePath());
        statement.bindString(8, entity.getNotes());
        statement.bindString(9, entity.getBatchId());
      }
    };
    this.__preparedStmtOfUpdateBatchStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE batches SET status = ? WHERE batchId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBatch(final Batch batch, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBatch.insert(batch);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBatchLogCrossRef(final BatchLogCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBatchLogCrossRef.insert(crossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBatch(final Batch batch, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBatch.handle(batch);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBatch(final Batch batch, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBatch.handle(batch);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBatchStatus(final String batchId, final BatchStatus status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBatchStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromBatchStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, batchId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateBatchStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Batch>> getAllBatches() {
    final String _sql = "SELECT * FROM batches ORDER BY createdDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"batches"}, new Callable<List<Batch>>() {
      @Override
      @NonNull
      public List<Batch> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final int _cursorIndexOfTotalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuantity");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQrCodePath = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCodePath");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<Batch> _result = new ArrayList<Batch>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Batch _item;
            final String _tmpBatchId;
            _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            final double _tmpTotalQuantity;
            _tmpTotalQuantity = _cursor.getDouble(_cursorIndexOfTotalQuantity);
            final String _tmpGrade;
            _tmpGrade = _cursor.getString(_cursorIndexOfGrade);
            final FloralSource _tmpFloralSource;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFloralSource);
            _tmpFloralSource = __converters.toFloralSource(_tmp);
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            final BatchStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toBatchStatus(_tmp_1);
            final String _tmpQrCodePath;
            _tmpQrCodePath = _cursor.getString(_cursorIndexOfQrCodePath);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new Batch(_tmpBatchId,_tmpTotalQuantity,_tmpGrade,_tmpFloralSource,_tmpCreatedDate,_tmpStatus,_tmpQrCodePath,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBatchById(final String batchId, final Continuation<? super Batch> $completion) {
    final String _sql = "SELECT * FROM batches WHERE batchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, batchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Batch>() {
      @Override
      @Nullable
      public Batch call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final int _cursorIndexOfTotalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuantity");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQrCodePath = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCodePath");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final Batch _result;
          if (_cursor.moveToFirst()) {
            final String _tmpBatchId;
            _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            final double _tmpTotalQuantity;
            _tmpTotalQuantity = _cursor.getDouble(_cursorIndexOfTotalQuantity);
            final String _tmpGrade;
            _tmpGrade = _cursor.getString(_cursorIndexOfGrade);
            final FloralSource _tmpFloralSource;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFloralSource);
            _tmpFloralSource = __converters.toFloralSource(_tmp);
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            final BatchStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toBatchStatus(_tmp_1);
            final String _tmpQrCodePath;
            _tmpQrCodePath = _cursor.getString(_cursorIndexOfQrCodePath);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _result = new Batch(_tmpBatchId,_tmpTotalQuantity,_tmpGrade,_tmpFloralSource,_tmpCreatedDate,_tmpStatus,_tmpQrCodePath,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBatchWithLogs(final String batchId,
      final Continuation<? super BatchWithLogs> $completion) {
    final String _sql = "SELECT * FROM batches WHERE batchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, batchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<BatchWithLogs>() {
      @Override
      @Nullable
      public BatchWithLogs call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
            final int _cursorIndexOfTotalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuantity");
            final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
            final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
            final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfQrCodePath = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCodePath");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final ArrayMap<String, ArrayList<HarvestLog>> _collectionHarvestLogs = new ArrayMap<String, ArrayList<HarvestLog>>();
            while (_cursor.moveToNext()) {
              final String _tmpKey;
              _tmpKey = _cursor.getString(_cursorIndexOfBatchId);
              if (!_collectionHarvestLogs.containsKey(_tmpKey)) {
                _collectionHarvestLogs.put(_tmpKey, new ArrayList<HarvestLog>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipharvestLogsAscomJenugumpuAppDataEntityHarvestLog(_collectionHarvestLogs);
            final BatchWithLogs _result;
            if (_cursor.moveToFirst()) {
              final Batch _tmpBatch;
              final String _tmpBatchId;
              _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
              final double _tmpTotalQuantity;
              _tmpTotalQuantity = _cursor.getDouble(_cursorIndexOfTotalQuantity);
              final String _tmpGrade;
              _tmpGrade = _cursor.getString(_cursorIndexOfGrade);
              final FloralSource _tmpFloralSource;
              final String _tmp;
              _tmp = _cursor.getString(_cursorIndexOfFloralSource);
              _tmpFloralSource = __converters.toFloralSource(_tmp);
              final long _tmpCreatedDate;
              _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
              final BatchStatus _tmpStatus;
              final String _tmp_1;
              _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
              _tmpStatus = __converters.toBatchStatus(_tmp_1);
              final String _tmpQrCodePath;
              _tmpQrCodePath = _cursor.getString(_cursorIndexOfQrCodePath);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              _tmpBatch = new Batch(_tmpBatchId,_tmpTotalQuantity,_tmpGrade,_tmpFloralSource,_tmpCreatedDate,_tmpStatus,_tmpQrCodePath,_tmpNotes);
              final ArrayList<HarvestLog> _tmpHarvestLogsCollection;
              final String _tmpKey_1;
              _tmpKey_1 = _cursor.getString(_cursorIndexOfBatchId);
              _tmpHarvestLogsCollection = _collectionHarvestLogs.get(_tmpKey_1);
              _result = new BatchWithLogs(_tmpBatch,_tmpHarvestLogsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BatchWithLogs>> getAllBatchesWithLogs() {
    final String _sql = "SELECT * FROM batches ORDER BY createdDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"batch_log_cross_ref",
        "harvest_logs", "batches"}, new Callable<List<BatchWithLogs>>() {
      @Override
      @NonNull
      public List<BatchWithLogs> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
            final int _cursorIndexOfTotalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuantity");
            final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
            final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
            final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfQrCodePath = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCodePath");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final ArrayMap<String, ArrayList<HarvestLog>> _collectionHarvestLogs = new ArrayMap<String, ArrayList<HarvestLog>>();
            while (_cursor.moveToNext()) {
              final String _tmpKey;
              _tmpKey = _cursor.getString(_cursorIndexOfBatchId);
              if (!_collectionHarvestLogs.containsKey(_tmpKey)) {
                _collectionHarvestLogs.put(_tmpKey, new ArrayList<HarvestLog>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipharvestLogsAscomJenugumpuAppDataEntityHarvestLog(_collectionHarvestLogs);
            final List<BatchWithLogs> _result = new ArrayList<BatchWithLogs>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final BatchWithLogs _item;
              final Batch _tmpBatch;
              final String _tmpBatchId;
              _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
              final double _tmpTotalQuantity;
              _tmpTotalQuantity = _cursor.getDouble(_cursorIndexOfTotalQuantity);
              final String _tmpGrade;
              _tmpGrade = _cursor.getString(_cursorIndexOfGrade);
              final FloralSource _tmpFloralSource;
              final String _tmp;
              _tmp = _cursor.getString(_cursorIndexOfFloralSource);
              _tmpFloralSource = __converters.toFloralSource(_tmp);
              final long _tmpCreatedDate;
              _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
              final BatchStatus _tmpStatus;
              final String _tmp_1;
              _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
              _tmpStatus = __converters.toBatchStatus(_tmp_1);
              final String _tmpQrCodePath;
              _tmpQrCodePath = _cursor.getString(_cursorIndexOfQrCodePath);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              _tmpBatch = new Batch(_tmpBatchId,_tmpTotalQuantity,_tmpGrade,_tmpFloralSource,_tmpCreatedDate,_tmpStatus,_tmpQrCodePath,_tmpNotes);
              final ArrayList<HarvestLog> _tmpHarvestLogsCollection;
              final String _tmpKey_1;
              _tmpKey_1 = _cursor.getString(_cursorIndexOfBatchId);
              _tmpHarvestLogsCollection = _collectionHarvestLogs.get(_tmpKey_1);
              _item = new BatchWithLogs(_tmpBatch,_tmpHarvestLogsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getTotalBatchStock() {
    final String _sql = "SELECT SUM(totalQuantity) FROM batches WHERE status != 'SOLD'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"batches"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipharvestLogsAscomJenugumpuAppDataEntityHarvestLog(
      @NonNull final ArrayMap<String, ArrayList<HarvestLog>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchArrayMap(_map, true, (map) -> {
        __fetchRelationshipharvestLogsAscomJenugumpuAppDataEntityHarvestLog(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `harvest_logs`.`id` AS `id`,`harvest_logs`.`quantity` AS `quantity`,`harvest_logs`.`floralSource` AS `floralSource`,`harvest_logs`.`grade` AS `grade`,`harvest_logs`.`location` AS `location`,`harvest_logs`.`date` AS `date`,`harvest_logs`.`notes` AS `notes`,`harvest_logs`.`isUsedInBatch` AS `isUsedInBatch`,_junction.`batchId` FROM `batch_log_cross_ref` AS _junction INNER JOIN `harvest_logs` ON (_junction.`harvestLogId` = `harvest_logs`.`id`) WHERE _junction.`batchId` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      _stmt.bindString(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.batchId;
      final int _itemKeyIndex = 8;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfQuantity = 1;
      final int _cursorIndexOfFloralSource = 2;
      final int _cursorIndexOfGrade = 3;
      final int _cursorIndexOfLocation = 4;
      final int _cursorIndexOfDate = 5;
      final int _cursorIndexOfNotes = 6;
      final int _cursorIndexOfIsUsedInBatch = 7;
      while (_cursor.moveToNext()) {
        final String _tmpKey;
        _tmpKey = _cursor.getString(_itemKeyIndex);
        final ArrayList<HarvestLog> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final HarvestLog _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final double _tmpQuantity;
          _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
          final FloralSource _tmpFloralSource;
          final String _tmp;
          _tmp = _cursor.getString(_cursorIndexOfFloralSource);
          _tmpFloralSource = __converters.toFloralSource(_tmp);
          final String _tmpGrade;
          _tmpGrade = _cursor.getString(_cursorIndexOfGrade);
          final String _tmpLocation;
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
          final long _tmpDate;
          _tmpDate = _cursor.getLong(_cursorIndexOfDate);
          final String _tmpNotes;
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
          final boolean _tmpIsUsedInBatch;
          final int _tmp_1;
          _tmp_1 = _cursor.getInt(_cursorIndexOfIsUsedInBatch);
          _tmpIsUsedInBatch = _tmp_1 != 0;
          _item_1 = new HarvestLog(_tmpId,_tmpQuantity,_tmpFloralSource,_tmpGrade,_tmpLocation,_tmpDate,_tmpNotes,_tmpIsUsedInBatch);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
