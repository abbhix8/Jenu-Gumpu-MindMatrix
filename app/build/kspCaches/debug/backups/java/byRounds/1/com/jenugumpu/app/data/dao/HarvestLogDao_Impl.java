package com.jenugumpu.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jenugumpu.app.data.database.Converters;
import com.jenugumpu.app.data.entity.FloralSource;
import com.jenugumpu.app.data.entity.HarvestLog;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HarvestLogDao_Impl implements HarvestLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HarvestLog> __insertionAdapterOfHarvestLog;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<HarvestLog> __deletionAdapterOfHarvestLog;

  private final EntityDeletionOrUpdateAdapter<HarvestLog> __updateAdapterOfHarvestLog;

  public HarvestLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHarvestLog = new EntityInsertionAdapter<HarvestLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `harvest_logs` (`id`,`quantity`,`floralSource`,`grade`,`location`,`date`,`notes`,`isUsedInBatch`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HarvestLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getQuantity());
        final String _tmp = __converters.fromFloralSource(entity.getFloralSource());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getGrade());
        statement.bindString(5, entity.getLocation());
        statement.bindLong(6, entity.getDate());
        statement.bindString(7, entity.getNotes());
        final int _tmp_1 = entity.isUsedInBatch() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
      }
    };
    this.__deletionAdapterOfHarvestLog = new EntityDeletionOrUpdateAdapter<HarvestLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `harvest_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HarvestLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHarvestLog = new EntityDeletionOrUpdateAdapter<HarvestLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `harvest_logs` SET `id` = ?,`quantity` = ?,`floralSource` = ?,`grade` = ?,`location` = ?,`date` = ?,`notes` = ?,`isUsedInBatch` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HarvestLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getQuantity());
        final String _tmp = __converters.fromFloralSource(entity.getFloralSource());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getGrade());
        statement.bindString(5, entity.getLocation());
        statement.bindLong(6, entity.getDate());
        statement.bindString(7, entity.getNotes());
        final int _tmp_1 = entity.isUsedInBatch() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insertHarvestLog(final HarvestLog harvestLog,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHarvestLog.insertAndReturnId(harvestLog);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHarvestLog(final HarvestLog harvestLog,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHarvestLog.handle(harvestLog);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateHarvestLog(final HarvestLog harvestLog,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHarvestLog.handle(harvestLog);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HarvestLog>> getAllHarvestLogs() {
    final String _sql = "SELECT * FROM harvest_logs ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_logs"}, new Callable<List<HarvestLog>>() {
      @Override
      @NonNull
      public List<HarvestLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsUsedInBatch = CursorUtil.getColumnIndexOrThrow(_cursor, "isUsedInBatch");
          final List<HarvestLog> _result = new ArrayList<HarvestLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HarvestLog _item;
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
            _item = new HarvestLog(_tmpId,_tmpQuantity,_tmpFloralSource,_tmpGrade,_tmpLocation,_tmpDate,_tmpNotes,_tmpIsUsedInBatch);
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
  public Object getHarvestLogById(final long id,
      final Continuation<? super HarvestLog> $completion) {
    final String _sql = "SELECT * FROM harvest_logs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HarvestLog>() {
      @Override
      @Nullable
      public HarvestLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsUsedInBatch = CursorUtil.getColumnIndexOrThrow(_cursor, "isUsedInBatch");
          final HarvestLog _result;
          if (_cursor.moveToFirst()) {
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
            _result = new HarvestLog(_tmpId,_tmpQuantity,_tmpFloralSource,_tmpGrade,_tmpLocation,_tmpDate,_tmpNotes,_tmpIsUsedInBatch);
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
  public Flow<List<HarvestLog>> getUnusedHarvestLogs() {
    final String _sql = "SELECT * FROM harvest_logs WHERE isUsedInBatch = 0 ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_logs"}, new Callable<List<HarvestLog>>() {
      @Override
      @NonNull
      public List<HarvestLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfFloralSource = CursorUtil.getColumnIndexOrThrow(_cursor, "floralSource");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsUsedInBatch = CursorUtil.getColumnIndexOrThrow(_cursor, "isUsedInBatch");
          final List<HarvestLog> _result = new ArrayList<HarvestLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HarvestLog _item;
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
            _item = new HarvestLog(_tmpId,_tmpQuantity,_tmpFloralSource,_tmpGrade,_tmpLocation,_tmpDate,_tmpNotes,_tmpIsUsedInBatch);
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
  public Flow<Double> getTotalAvailableStock() {
    final String _sql = "SELECT SUM(quantity) FROM harvest_logs WHERE isUsedInBatch = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_logs"}, new Callable<Double>() {
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

  @Override
  public Flow<Double> getStockByFloralSource(final FloralSource source) {
    final String _sql = "SELECT SUM(quantity) FROM harvest_logs WHERE floralSource = ? AND isUsedInBatch = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromFloralSource(source);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_logs"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp_1;
            if (_cursor.isNull(0)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getDouble(0);
            }
            _result = _tmp_1;
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

  @Override
  public Object markLogsAsUsed(final List<Long> logIds,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE harvest_logs SET isUsedInBatch = 1 WHERE id IN (");
        final int _inputSize = logIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (long _item : logIds) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
