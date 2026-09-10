.class public final synthetic Lf53;
.super Ljava/lang/Object;
.source "SourceFile"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field public final synthetic a:I

.field public final synthetic b:Ljava/lang/Object;

.field public final synthetic c:Ljava/lang/Object;


# direct methods
.method public synthetic constructor <init>(ILjava/lang/Object;Ljava/lang/Object;)V
    .locals 0

    .line 12
    iput p1, p0, Lf53;->a:I

    iput-object p2, p0, Lf53;->b:Ljava/lang/Object;

    iput-object p3, p0, Lf53;->c:Ljava/lang/Object;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public constructor <init>(Lcom/google/android/gms/measurement/internal/zzhw;Lcom/google/android/gms/internal/measurement/zzbz;Lcom/google/android/gms/measurement/internal/zzhw;)V
    .locals 0

    const/4 p3, 0x4

    iput p3, p0, Lf53;->a:I

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p2, p0, Lf53;->b:Ljava/lang/Object;

    iput-object p1, p0, Lf53;->c:Ljava/lang/Object;

    return-void
.end method

.method public synthetic constructor <init>(Ljava/lang/Object;Ljava/lang/Object;IZ)V
    .locals 0

    .line 11
    iput p3, p0, Lf53;->a:I

    iput-object p2, p0, Lf53;->b:Ljava/lang/Object;

    iput-object p1, p0, Lf53;->c:Ljava/lang/Object;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method private final a()V
    .locals 39

    move-object/from16 v0, p0

    const-string v1, "admob_app_id"

    iget-object v2, v0, Lf53;->c:Ljava/lang/Object;

    check-cast v2, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v3, v2, Lcom/google/android/gms/measurement/internal/zzim;->s:Lcom/google/android/gms/measurement/internal/zzak;

    iget-object v4, v2, Lcom/google/android/gms/measurement/internal/zzim;->u:Lcom/google/android/gms/measurement/internal/zzhc;

    iget-object v5, v2, Lcom/google/android/gms/measurement/internal/zzim;->t:Lw63;

    iget-object v0, v0, Lf53;->b:Ljava/lang/Object;

    move-object v6, v0

    check-cast v6, Lcom/google/android/gms/measurement/internal/zzkd;

    iget-object v7, v2, Lcom/google/android/gms/measurement/internal/zzim;->v:Lcom/google/android/gms/measurement/internal/zzij;

    iget-object v8, v2, Lcom/google/android/gms/measurement/internal/zzim;->R:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-static {v7}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    invoke-virtual {v7}, Lcom/google/android/gms/measurement/internal/zzij;->j()V

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzbd;

    invoke-direct {v0, v2}, Ll73;-><init>(Lcom/google/android/gms/measurement/internal/zzim;)V

    invoke-virtual {v0}, Ll73;->m()V

    iput-object v0, v2, Lcom/google/android/gms/measurement/internal/zzim;->H:Lcom/google/android/gms/measurement/internal/zzbd;

    new-instance v9, Lcom/google/android/gms/measurement/internal/zzgr;

    iget-wide v10, v6, Lcom/google/android/gms/measurement/internal/zzkd;->f:J

    invoke-direct {v9, v2}, Lp63;-><init>(Lcom/google/android/gms/measurement/internal/zzim;)V

    const-wide/16 v12, 0x0

    iput-wide v12, v9, Lcom/google/android/gms/measurement/internal/zzgr;->C:J

    const/4 v14, 0x0

    iput-object v14, v9, Lcom/google/android/gms/measurement/internal/zzgr;->D:Ljava/lang/String;

    iput-wide v10, v9, Lcom/google/android/gms/measurement/internal/zzgr;->v:J

    invoke-virtual {v9}, Lp63;->q()V

    iput-object v9, v2, Lcom/google/android/gms/measurement/internal/zzim;->I:Lcom/google/android/gms/measurement/internal/zzgr;

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzgu;

    invoke-direct {v0, v2}, Lcom/google/android/gms/measurement/internal/zzgu;-><init>(Lcom/google/android/gms/measurement/internal/zzim;)V

    invoke-virtual {v0}, Lp63;->q()V

    iput-object v0, v2, Lcom/google/android/gms/measurement/internal/zzim;->F:Lcom/google/android/gms/measurement/internal/zzgu;

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzmp;

    invoke-direct {v0, v2}, Lcom/google/android/gms/measurement/internal/zzmp;-><init>(Lcom/google/android/gms/measurement/internal/zzim;)V

    invoke-virtual {v0}, Lp63;->q()V

    iput-object v0, v2, Lcom/google/android/gms/measurement/internal/zzim;->G:Lcom/google/android/gms/measurement/internal/zzmp;

    iget-object v10, v2, Lcom/google/android/gms/measurement/internal/zzim;->x:Lcom/google/android/gms/measurement/internal/zzqd;

    iget-boolean v0, v10, Ll73;->c:Z

    const-string v11, "Can\'t initialize twice"

    if-nez v0, :cond_52

    invoke-virtual {v10}, Lmv;->j()V

    new-instance v0, Ljava/security/SecureRandom;

    invoke-direct {v0}, Ljava/security/SecureRandom;-><init>()V

    invoke-virtual {v0}, Ljava/util/Random;->nextLong()J

    move-result-wide v15

    cmp-long v17, v15, v12

    if-nez v17, :cond_0

    invoke-virtual {v0}, Ljava/util/Random;->nextLong()J

    move-result-wide v15

    cmp-long v0, v15, v12

    if-nez v0, :cond_0

    invoke-virtual {v10}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->v:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v12, "Utils falling back to Random for random id"

    invoke-virtual {v0, v12}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_0
    move-wide v12, v15

    iget-object v0, v10, Lcom/google/android/gms/measurement/internal/zzqd;->e:Ljava/util/concurrent/atomic/AtomicLong;

    invoke-virtual {v0, v12, v13}, Ljava/util/concurrent/atomic/AtomicLong;->set(J)V

    iget-object v0, v10, Lmv;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzim;->R:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicInteger;->incrementAndGet()I

    const/4 v12, 0x1

    iput-boolean v12, v10, Ll73;->c:Z

    iget-boolean v0, v5, Ll73;->c:Z

    if-nez v0, :cond_51

    iget-object v0, v5, Lmv;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    const-string v13, "com.google.android.gms.measurement.prefs"

    const/4 v15, 0x0

    invoke-virtual {v0, v13, v15}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v0

    iput-object v0, v5, Lw63;->d:Landroid/content/SharedPreferences;

    const-string v13, "has_been_opened"

    invoke-interface {v0, v13, v15}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v0

    iput-boolean v0, v5, Lw63;->F:Z

    if-nez v0, :cond_1

    iget-object v0, v5, Lw63;->d:Landroid/content/SharedPreferences;

    invoke-interface {v0}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v0

    invoke-interface {v0, v13, v12}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v0}, Landroid/content/SharedPreferences$Editor;->apply()V

    :cond_1
    new-instance v0, Lcom/google/android/gms/measurement/internal/zzhs;

    sget-object v13, Lcom/google/android/gms/measurement/internal/zzbl;->d:Lcom/google/android/gms/measurement/internal/zzgi;

    invoke-virtual {v13, v14}, Lcom/google/android/gms/measurement/internal/zzgi;->a(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v13

    check-cast v13, Ljava/lang/Long;

    invoke-virtual {v13}, Ljava/lang/Long;->longValue()J

    move-result-wide v14

    const-wide/16 v12, 0x0

    invoke-static {v12, v13, v14, v15}, Ljava/lang/Math;->max(JJ)J

    move-result-wide v14

    invoke-direct {v0, v5, v14, v15}, Lcom/google/android/gms/measurement/internal/zzhs;-><init>(Lw63;J)V

    iput-object v0, v5, Lw63;->s:Lcom/google/android/gms/measurement/internal/zzhs;

    iget-object v0, v5, Lmv;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzim;->R:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicInteger;->incrementAndGet()I

    const/4 v12, 0x1

    iput-boolean v12, v5, Ll73;->c:Z

    iget-object v12, v2, Lcom/google/android/gms/measurement/internal/zzim;->I:Lcom/google/android/gms/measurement/internal/zzgr;

    iget-boolean v0, v12, Lp63;->c:Z

    if-nez v0, :cond_50

    iget-object v0, v12, Lmv;->b:Ljava/lang/Object;

    move-object v13, v0

    check-cast v13, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v0, v13, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    iget-object v14, v13, Lcom/google/android/gms/measurement/internal/zzim;->E:Ljava/lang/String;

    iget-object v15, v13, Lcom/google/android/gms/measurement/internal/zzim;->b:Ljava/lang/String;

    move-object/from16 v19, v7

    invoke-virtual {v0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v7

    move-object/from16 v20, v8

    iget-object v8, v13, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    move-object/from16 v21, v9

    invoke-virtual {v8}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v9

    const-string v0, ""

    const-string v22, "unknown"

    const-string v23, "Unknown"

    const/high16 v24, -0x80000000

    if-nez v9, :cond_2

    move-object/from16 v25, v11

    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v11

    iget-object v11, v11, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    move-object/from16 v26, v5

    const-string v5, "PackageManager is null, app identity information might be inaccurate. appId"

    move-object/from16 v27, v6

    invoke-static {v7}, Lcom/google/android/gms/measurement/internal/zzhc;->q(Ljava/lang/String;)Ls63;

    move-result-object v6

    invoke-virtual {v11, v5, v6}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    move-object/from16 v28, v9

    move-object/from16 v5, v22

    move-object/from16 v6, v23

    move-object v9, v6

    :goto_0
    move/from16 v11, v24

    goto/16 :goto_7

    :cond_2
    move-object/from16 v26, v5

    move-object/from16 v27, v6

    move-object/from16 v25, v11

    :try_start_0
    invoke-virtual {v9, v7}, Landroid/content/pm/PackageManager;->getInstallerPackageName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v22
    :try_end_0
    .catch Ljava/lang/IllegalArgumentException; {:try_start_0 .. :try_end_0} :catch_0

    :goto_1
    move-object/from16 v5, v22

    goto :goto_2

    :catch_0
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v5

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Error retrieving app installer package name. appId"

    invoke-static {v7}, Lcom/google/android/gms/measurement/internal/zzhc;->q(Ljava/lang/String;)Ls63;

    move-result-object v11

    invoke-virtual {v5, v6, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    goto :goto_1

    :goto_2
    if-nez v5, :cond_4

    const-string v5, "manual_install"

    :cond_3
    move-object/from16 v22, v5

    goto :goto_3

    :cond_4
    const-string v6, "com.android.vending"

    invoke-virtual {v6, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_3

    move-object/from16 v22, v0

    :goto_3
    :try_start_1
    invoke-virtual {v8}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v5

    const/4 v6, 0x0

    invoke-virtual {v9, v5, v6}, Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;

    move-result-object v5

    if-eqz v5, :cond_6

    iget-object v6, v5, Landroid/content/pm/PackageInfo;->applicationInfo:Landroid/content/pm/ApplicationInfo;

    invoke-virtual {v9, v6}, Landroid/content/pm/PackageManager;->getApplicationLabel(Landroid/content/pm/ApplicationInfo;)Ljava/lang/CharSequence;

    move-result-object v6

    invoke-static {v6}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v11

    if-nez v11, :cond_5

    invoke-virtual {v6}, Ljava/lang/Object;->toString()Ljava/lang/String;

    move-result-object v6
    :try_end_1
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_1 .. :try_end_1} :catch_2

    goto :goto_4

    :cond_5
    move-object/from16 v6, v23

    :goto_4
    :try_start_2
    iget-object v11, v5, Landroid/content/pm/PackageInfo;->versionName:Ljava/lang/String;
    :try_end_2
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_2 .. :try_end_2} :catch_3

    :try_start_3
    iget v5, v5, Landroid/content/pm/PackageInfo;->versionCode:I
    :try_end_3
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_3 .. :try_end_3} :catch_1

    move/from16 v24, v5

    move-object/from16 v23, v11

    goto :goto_5

    :catch_1
    move-object/from16 v23, v11

    goto :goto_6

    :cond_6
    move-object/from16 v6, v23

    :goto_5
    move-object/from16 v28, v9

    move-object/from16 v5, v22

    move/from16 v11, v24

    move-object v9, v6

    move-object/from16 v6, v23

    goto :goto_7

    :catch_2
    move-object/from16 v6, v23

    :catch_3
    :goto_6
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v5

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "Error retrieving package info. appId, appName"

    move-object/from16 v28, v9

    invoke-static {v7}, Lcom/google/android/gms/measurement/internal/zzhc;->q(Ljava/lang/String;)Ls63;

    move-result-object v9

    invoke-virtual {v5, v11, v9, v6}, Lcom/google/android/gms/measurement/internal/zzhe;->c(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V

    move-object v9, v6

    move-object/from16 v5, v22

    move-object/from16 v6, v23

    goto/16 :goto_0

    :goto_7
    iput-object v7, v12, Lcom/google/android/gms/measurement/internal/zzgr;->d:Ljava/lang/String;

    iput-object v5, v12, Lcom/google/android/gms/measurement/internal/zzgr;->s:Ljava/lang/String;

    iput-object v6, v12, Lcom/google/android/gms/measurement/internal/zzgr;->e:Ljava/lang/String;

    iput v11, v12, Lcom/google/android/gms/measurement/internal/zzgr;->f:I

    iput-object v9, v12, Lcom/google/android/gms/measurement/internal/zzgr;->t:Ljava/lang/String;

    const-wide/16 v5, 0x0

    iput-wide v5, v12, Lcom/google/android/gms/measurement/internal/zzgr;->u:J

    invoke-static {v15}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v5

    if-nez v5, :cond_7

    const-string v5, "am"

    iget-object v6, v13, Lcom/google/android/gms/measurement/internal/zzim;->c:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_7

    const/4 v5, 0x1

    goto :goto_8

    :cond_7
    const/4 v5, 0x0

    :goto_8
    invoke-virtual {v13}, Lcom/google/android/gms/measurement/internal/zzim;->h()I

    move-result v6

    if-eqz v6, :cond_e

    const/4 v9, 0x1

    if-eq v6, v9, :cond_d

    const/4 v9, 0x3

    if-eq v6, v9, :cond_c

    const/4 v9, 0x4

    if-eq v6, v9, :cond_b

    const/4 v9, 0x6

    if-eq v6, v9, :cond_a

    const/4 v9, 0x7

    if-eq v6, v9, :cond_9

    const/16 v9, 0x8

    if-eq v6, v9, :cond_8

    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement disabled"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->t:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "Invalid scion state in identity"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_8
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement disabled due to denied storage consent"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_9
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement disabled via the global data collection setting"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_a
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->x:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement deactivated via resources. This method is being deprecated. Please refer to https://firebase.google.com/support/guides/disable-analytics"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_b
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement disabled via the manifest"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_c
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement disabled by setAnalyticsCollectionEnabled(false)"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_d
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement deactivated via the manifest"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_9

    :cond_e
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "App measurement collection enabled"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :goto_9
    if-nez v6, :cond_f

    const/4 v6, 0x1

    goto :goto_a

    :cond_f
    const/4 v6, 0x0

    :goto_a
    iput-object v0, v12, Lcom/google/android/gms/measurement/internal/zzgr;->z:Ljava/lang/String;

    iput-object v0, v12, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    if-eqz v5, :cond_10

    iput-object v15, v12, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    :cond_10
    :try_start_4
    const-string v5, "google_app_id"

    new-instance v9, Lcom/google/android/gms/measurement/internal/zzig;

    invoke-direct {v9, v8, v14}, Lcom/google/android/gms/measurement/internal/zzig;-><init>(Landroid/content/Context;Ljava/lang/String;)V

    invoke-virtual {v9, v5}, Lcom/google/android/gms/measurement/internal/zzig;->a(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v9

    if-eqz v9, :cond_11

    goto :goto_b

    :cond_11
    move-object v0, v5

    :goto_b
    iput-object v0, v12, Lcom/google/android/gms/measurement/internal/zzgr;->z:Ljava/lang/String;

    invoke-static {v5}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_12

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzig;

    invoke-direct {v0, v8, v14}, Lcom/google/android/gms/measurement/internal/zzig;-><init>(Landroid/content/Context;Ljava/lang/String;)V

    invoke-virtual {v0, v1}, Lcom/google/android/gms/measurement/internal/zzig;->a(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, v12, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    goto :goto_c

    :catch_4
    move-exception v0

    goto :goto_f

    :cond_12
    :goto_c
    if-eqz v6, :cond_14

    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v5, "App measurement enabled for app package, google app id"

    iget-object v6, v12, Lcom/google/android/gms/measurement/internal/zzgr;->d:Ljava/lang/String;

    iget-object v9, v12, Lcom/google/android/gms/measurement/internal/zzgr;->z:Ljava/lang/String;

    invoke-static {v9}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v9

    if-eqz v9, :cond_13

    iget-object v9, v12, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    goto :goto_d

    :cond_13
    iget-object v9, v12, Lcom/google/android/gms/measurement/internal/zzgr;->z:Ljava/lang/String;

    :goto_d
    invoke-virtual {v0, v5, v6, v9}, Lcom/google/android/gms/measurement/internal/zzhe;->c(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
    :try_end_4
    .catch Ljava/lang/IllegalStateException; {:try_start_4 .. :try_end_4} :catch_4

    :cond_14
    :goto_e
    const/4 v5, 0x0

    goto :goto_10

    :goto_f
    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v5

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Fetching Google App Id failed with exception. appId"

    invoke-static {v7}, Lcom/google/android/gms/measurement/internal/zzhc;->q(Ljava/lang/String;)Ls63;

    move-result-object v7

    invoke-virtual {v5, v6, v7, v0}, Lcom/google/android/gms/measurement/internal/zzhe;->c(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V

    goto :goto_e

    :goto_10
    iput-object v5, v12, Lcom/google/android/gms/measurement/internal/zzgr;->w:Ljava/util/List;

    iget-object v5, v13, Lcom/google/android/gms/measurement/internal/zzim;->s:Lcom/google/android/gms/measurement/internal/zzak;

    invoke-virtual {v5}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    const-string v0, "analytics.safelisted_events"

    invoke-static {v0}, Lcom/google/android/gms/common/internal/Preconditions;->e(Ljava/lang/String;)V

    invoke-virtual {v5}, Lcom/google/android/gms/measurement/internal/zzak;->n()Landroid/os/Bundle;

    move-result-object v6

    if-nez v6, :cond_15

    invoke-virtual {v5}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Failed to load metadata: Metadata bundle is null"

    invoke-virtual {v0, v6}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :goto_11
    const/4 v0, 0x0

    goto :goto_12

    :cond_15
    invoke-virtual {v6, v0}, Landroid/os/BaseBundle;->containsKey(Ljava/lang/String;)Z

    move-result v7

    if-nez v7, :cond_16

    goto :goto_11

    :cond_16
    invoke-virtual {v6, v0}, Landroid/os/BaseBundle;->getInt(Ljava/lang/String;)I

    move-result v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    :goto_12
    if-nez v0, :cond_17

    :goto_13
    const/4 v0, 0x0

    goto :goto_14

    :cond_17
    :try_start_5
    iget-object v6, v5, Lmv;->b:Ljava/lang/Object;

    check-cast v6, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v6, v6, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    invoke-virtual {v6}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v6

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-virtual {v6, v0}, Landroid/content/res/Resources;->getStringArray(I)[Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_18

    goto :goto_13

    :cond_18
    invoke-static {v0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object v0
    :try_end_5
    .catch Landroid/content/res/Resources$NotFoundException; {:try_start_5 .. :try_end_5} :catch_5

    goto :goto_14

    :catch_5
    move-exception v0

    invoke-virtual {v5}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v5

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Failed to load string array from metadata: resource not found"

    invoke-virtual {v5, v6, v0}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    goto :goto_13

    :goto_14
    if-eqz v0, :cond_1b

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v5

    if-eqz v5, :cond_19

    invoke-virtual {v12}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->x:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v5, "Safelisted event list is empty. Ignoring"

    invoke-virtual {v0, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_15

    :cond_19
    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v5

    :cond_1a
    invoke-interface {v5}, Ljava/util/Iterator;->hasNext()Z

    move-result v6

    if-eqz v6, :cond_1b

    invoke-interface {v5}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    invoke-virtual {v12}, Lmv;->i()Lcom/google/android/gms/measurement/internal/zzqd;

    move-result-object v7

    const-string v9, "safelisted event"

    invoke-virtual {v7, v9, v6}, Lcom/google/android/gms/measurement/internal/zzqd;->h0(Ljava/lang/String;Ljava/lang/String;)Z

    move-result v6

    if-nez v6, :cond_1a

    goto :goto_15

    :cond_1b
    iput-object v0, v12, Lcom/google/android/gms/measurement/internal/zzgr;->w:Ljava/util/List;

    :goto_15
    if-eqz v28, :cond_1c

    invoke-static {v8}, Lcom/google/android/gms/common/wrappers/InstantApps;->a(Landroid/content/Context;)Z

    move-result v0

    iput v0, v12, Lcom/google/android/gms/measurement/internal/zzgr;->y:I

    goto :goto_16

    :cond_1c
    const/4 v6, 0x0

    iput v6, v12, Lcom/google/android/gms/measurement/internal/zzgr;->y:I

    :goto_16
    iget-object v0, v12, Lmv;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzim;->R:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicInteger;->incrementAndGet()I

    const/4 v9, 0x1

    iput-boolean v9, v12, Lp63;->c:Z

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzmd;

    invoke-direct {v0, v2}, Lp63;-><init>(Lcom/google/android/gms/measurement/internal/zzim;)V

    invoke-virtual {v0}, Lp63;->q()V

    iput-object v0, v2, Lcom/google/android/gms/measurement/internal/zzim;->J:Lcom/google/android/gms/measurement/internal/zzmd;

    iget-boolean v5, v0, Lp63;->c:Z

    if-nez v5, :cond_4f

    iget-object v5, v0, Lmv;->b:Ljava/lang/Object;

    check-cast v5, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    const-string v6, "jobscheduler"

    invoke-virtual {v5, v6}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/app/job/JobScheduler;

    iput-object v5, v0, Lcom/google/android/gms/measurement/internal/zzmd;->d:Landroid/app/job/JobScheduler;

    iget-object v5, v0, Lmv;->b:Ljava/lang/Object;

    check-cast v5, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzim;->R:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v5}, Ljava/util/concurrent/atomic/AtomicInteger;->incrementAndGet()I

    const/4 v9, 0x1

    iput-boolean v9, v0, Lp63;->c:Z

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v0, v4, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-wide/32 v5, 0x1ccf3

    invoke-static {v5, v6}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v5

    const-string v6, "App measurement initialized, version"

    invoke-virtual {v0, v6, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    const-string v5, "To enable debug logging run: adb shell setprop log.tag.FA VERBOSE"

    invoke-virtual {v0, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    invoke-virtual/range {v21 .. v21}, Lcom/google/android/gms/measurement/internal/zzgr;->r()Ljava/lang/String;

    move-result-object v5

    iget-object v6, v2, Lcom/google/android/gms/measurement/internal/zzim;->b:Ljava/lang/String;

    invoke-static {v6}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v6

    if-eqz v6, :cond_1e

    iget-object v6, v3, Lcom/google/android/gms/measurement/internal/zzak;->d:Ljava/lang/String;

    invoke-virtual {v10, v5, v6}, Lcom/google/android/gms/measurement/internal/zzqd;->m0(Ljava/lang/String;Ljava/lang/String;)Z

    move-result v6

    if-eqz v6, :cond_1d

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    const-string v5, "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none."

    invoke-virtual {v0, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_17

    :cond_1d
    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    new-instance v6, Ljava/lang/StringBuilder;

    const-string v7, "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app "

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v0, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_1e
    :goto_17
    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v0, v4, Lcom/google/android/gms/measurement/internal/zzhc;->z:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v5, "Debug-level message logging enabled"

    invoke-virtual {v0, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    iget v0, v2, Lcom/google/android/gms/measurement/internal/zzim;->P:I

    invoke-virtual/range {v20 .. v20}, Ljava/util/concurrent/atomic/AtomicInteger;->get()I

    move-result v5

    if-eq v0, v5, :cond_1f

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v0, v4, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    iget v5, v2, Lcom/google/android/gms/measurement/internal/zzim;->P:I

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-virtual/range {v20 .. v20}, Ljava/util/concurrent/atomic/AtomicInteger;->get()I

    move-result v6

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    const-string v7, "Not all components initialized"

    invoke-virtual {v0, v7, v5, v6}, Lcom/google/android/gms/measurement/internal/zzhe;->c(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V

    :cond_1f
    const/4 v9, 0x1

    iput-boolean v9, v2, Lcom/google/android/gms/measurement/internal/zzim;->K:Z

    move-object/from16 v5, v27

    iget-object v0, v5, Lcom/google/android/gms/measurement/internal/zzkd;->g:Lcom/google/android/gms/internal/measurement/zzdz;

    iget-object v5, v2, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    iget-wide v6, v2, Lcom/google/android/gms/measurement/internal/zzim;->S:J

    iget-object v8, v2, Lcom/google/android/gms/measurement/internal/zzim;->B:Lcom/google/android/gms/measurement/internal/zzkf;

    invoke-static/range {v19 .. v19}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    invoke-virtual/range {v19 .. v19}, Lcom/google/android/gms/measurement/internal/zzij;->j()V

    sget-object v9, Lcom/google/android/gms/measurement/internal/zzbl;->Q0:Lcom/google/android/gms/measurement/internal/zzgi;

    const/4 v11, 0x0

    invoke-virtual {v3, v11, v9}, Lcom/google/android/gms/measurement/internal/zzak;->t(Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzgi;)Z

    move-result v12

    if-eqz v12, :cond_20

    invoke-virtual {v2}, Lcom/google/android/gms/measurement/internal/zzim;->m()Lcom/google/android/gms/measurement/internal/zzmd;

    move-result-object v12

    invoke-virtual {v12}, Lcom/google/android/gms/measurement/internal/zzmd;->s()Lcom/google/android/gms/internal/measurement/zzgg$zzo$zzb;

    move-result-object v12

    sget-object v13, Lcom/google/android/gms/internal/measurement/zzgg$zzo$zzb;->zzb:Lcom/google/android/gms/internal/measurement/zzgg$zzo$zzb;

    if-ne v12, v13, :cond_20

    const/4 v12, 0x1

    goto :goto_18

    :cond_20
    const/4 v12, 0x0

    :goto_18
    invoke-static {}, Lcom/google/android/gms/internal/measurement/zzpf;->zza()Z

    move-result v13

    const-wide/16 v19, 0x1

    if-eqz v13, :cond_21

    sget-object v13, Lcom/google/android/gms/measurement/internal/zzbl;->V0:Lcom/google/android/gms/measurement/internal/zzgi;

    const/4 v15, 0x0

    invoke-virtual {v3, v15, v13}, Lcom/google/android/gms/measurement/internal/zzak;->t(Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzgi;)Z

    move-result v13

    if-eqz v13, :cond_21

    invoke-static {v10}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    invoke-virtual {v10}, Lmv;->j()V

    invoke-virtual {v10}, Lcom/google/android/gms/measurement/internal/zzqd;->s0()J

    move-result-wide v21

    cmp-long v13, v21, v19

    if-nez v13, :cond_21

    goto :goto_19

    :cond_21
    if-eqz v12, :cond_23

    :goto_19
    invoke-static {v10}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    iget-object v13, v10, Lmv;->b:Ljava/lang/Object;

    check-cast v13, Lcom/google/android/gms/measurement/internal/zzim;

    invoke-virtual {v10}, Lmv;->j()V

    new-instance v15, Landroid/content/IntentFilter;

    invoke-direct {v15}, Landroid/content/IntentFilter;-><init>()V

    const-string v11, "com.google.android.gms.measurement.TRIGGERS_AVAILABLE"

    invoke-virtual {v15, v11}, Landroid/content/IntentFilter;->addAction(Ljava/lang/String;)V

    iget-object v11, v13, Lcom/google/android/gms/measurement/internal/zzim;->s:Lcom/google/android/gms/measurement/internal/zzak;

    const/4 v14, 0x0

    invoke-virtual {v11, v14, v9}, Lcom/google/android/gms/measurement/internal/zzak;->t(Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzgi;)Z

    move-result v9

    if-eqz v9, :cond_22

    const-string v9, "com.google.android.gms.measurement.BATCHES_AVAILABLE"

    invoke-virtual {v15, v9}, Landroid/content/IntentFilter;->addAction(Ljava/lang/String;)V

    :cond_22
    new-instance v9, Lcom/google/android/gms/measurement/internal/zzs;

    invoke-direct {v9, v13}, Lcom/google/android/gms/measurement/internal/zzs;-><init>(Lcom/google/android/gms/measurement/internal/zzim;)V

    iget-object v11, v13, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    const/4 v13, 0x2

    invoke-static {v11, v9, v15, v13}, Landroidx/core/content/ContextCompat;->registerReceiver(Landroid/content/Context;Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;I)Landroid/content/Intent;

    invoke-virtual {v10}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v9

    iget-object v9, v9, Lcom/google/android/gms/measurement/internal/zzhc;->z:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v11, "Registered app receiver"

    invoke-virtual {v9, v11}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_23
    if-eqz v12, :cond_24

    invoke-virtual {v2}, Lcom/google/android/gms/measurement/internal/zzim;->m()Lcom/google/android/gms/measurement/internal/zzmd;

    move-result-object v9

    sget-object v11, Lcom/google/android/gms/measurement/internal/zzbl;->C:Lcom/google/android/gms/measurement/internal/zzgi;

    const/4 v15, 0x0

    invoke-virtual {v11, v15}, Lcom/google/android/gms/measurement/internal/zzgi;->a(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Ljava/lang/Long;

    invoke-virtual {v11}, Ljava/lang/Long;->longValue()J

    move-result-wide v11

    invoke-virtual {v9, v11, v12}, Lcom/google/android/gms/measurement/internal/zzmd;->r(J)V

    :cond_24
    invoke-static/range {v26 .. v26}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    move-object/from16 v9, v26

    iget-object v11, v9, Lw63;->J:Lcom/google/android/gms/measurement/internal/zzhr;

    iget-object v12, v9, Lw63;->u:Lcom/google/android/gms/measurement/internal/zzhr;

    iget-object v13, v9, Lw63;->t:Lcom/google/android/gms/measurement/internal/zzhp;

    invoke-virtual {v9}, Lw63;->u()Lcom/google/android/gms/measurement/internal/zzju;

    move-result-object v14

    iget v15, v14, Lcom/google/android/gms/measurement/internal/zzju;->b:I

    move-object/from16 v22, v5

    const-string v5, "google_analytics_default_allow_ad_storage"

    move-object/from16 v23, v14

    const/4 v14, 0x0

    invoke-virtual {v3, v5, v14}, Lcom/google/android/gms/measurement/internal/zzak;->q(Ljava/lang/String;Z)Lcom/google/android/gms/measurement/internal/zzjx;

    move-result-object v5

    move-object/from16 v24, v11

    const-string v11, "google_analytics_default_allow_analytics_storage"

    invoke-virtual {v3, v11, v14}, Lcom/google/android/gms/measurement/internal/zzak;->q(Ljava/lang/String;Z)Lcom/google/android/gms/measurement/internal/zzjx;

    move-result-object v11

    const-string v14, "consent_source"

    move-object/from16 v26, v12

    sget-object v12, Lcom/google/android/gms/measurement/internal/zzjx;->b:Lcom/google/android/gms/measurement/internal/zzjx;

    move-object/from16 v34, v2

    sget-object v2, Lcom/google/android/gms/measurement/internal/zzju$zza;->c:Lcom/google/android/gms/measurement/internal/zzju$zza;

    move-object/from16 v35, v1

    const-class v1, Lcom/google/android/gms/measurement/internal/zzju$zza;

    move-object/from16 v36, v10

    if-ne v5, v12, :cond_26

    if-eq v11, v12, :cond_25

    goto :goto_1a

    :cond_25
    move-wide/from16 v37, v6

    goto :goto_1b

    :cond_26
    :goto_1a
    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v10

    move-wide/from16 v37, v6

    const/16 v6, 0x64

    invoke-interface {v10, v14, v6}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v7

    const/16 v6, -0xa

    invoke-static {v6, v7}, Lcom/google/android/gms/measurement/internal/zzju;->h(II)Z

    move-result v7

    if-eqz v7, :cond_27

    new-instance v7, Ljava/util/EnumMap;

    invoke-direct {v7, v1}, Ljava/util/EnumMap;-><init>(Ljava/lang/Class;)V

    sget-object v10, Lcom/google/android/gms/measurement/internal/zzju$zza;->b:Lcom/google/android/gms/measurement/internal/zzju$zza;

    invoke-virtual {v7, v10, v5}, Ljava/util/EnumMap;->put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v2, v11}, Ljava/util/EnumMap;->put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;

    new-instance v5, Lcom/google/android/gms/measurement/internal/zzju;

    invoke-direct {v5, v7, v6}, Lcom/google/android/gms/measurement/internal/zzju;-><init>(Ljava/util/EnumMap;I)V

    const/4 v6, 0x0

    goto/16 :goto_1f

    :cond_27
    :goto_1b
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v5

    invoke-virtual {v5}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v5

    if-nez v5, :cond_28

    if-eqz v15, :cond_29

    const/16 v5, 0x1e

    if-eq v15, v5, :cond_29

    const/16 v6, 0xa

    if-eq v15, v6, :cond_29

    if-eq v15, v5, :cond_29

    if-eq v15, v5, :cond_29

    const/16 v5, 0x28

    if-ne v15, v5, :cond_28

    goto :goto_1c

    :cond_28
    const/4 v6, 0x0

    goto :goto_1d

    :cond_29
    :goto_1c
    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    new-instance v5, Lcom/google/android/gms/measurement/internal/zzju;

    const/16 v6, -0xa

    invoke-direct {v5, v6}, Lcom/google/android/gms/measurement/internal/zzju;-><init>(I)V

    const/4 v6, 0x0

    invoke-virtual {v8, v5, v6}, Lcom/google/android/gms/measurement/internal/zzkf;->x(Lcom/google/android/gms/measurement/internal/zzju;Z)V

    goto :goto_1e

    :goto_1d
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v5

    invoke-virtual {v5}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v5

    if-eqz v5, :cond_2b

    if-eqz v0, :cond_2b

    iget-object v5, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zzg:Landroid/os/Bundle;

    if-eqz v5, :cond_2b

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v5

    const/16 v7, 0x64

    invoke-interface {v5, v14, v7}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v5

    const/16 v7, 0x1e

    invoke-static {v7, v5}, Lcom/google/android/gms/measurement/internal/zzju;->h(II)Z

    move-result v5

    if-eqz v5, :cond_2b

    iget-object v5, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zzg:Landroid/os/Bundle;

    invoke-static {v7, v5}, Lcom/google/android/gms/measurement/internal/zzju;->b(ILandroid/os/Bundle;)Lcom/google/android/gms/measurement/internal/zzju;

    move-result-object v5

    iget-object v7, v5, Lcom/google/android/gms/measurement/internal/zzju;->a:Ljava/util/EnumMap;

    invoke-virtual {v7}, Ljava/util/EnumMap;->values()Ljava/util/Collection;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/Collection;->iterator()Ljava/util/Iterator;

    move-result-object v7

    :cond_2a
    invoke-interface {v7}, Ljava/util/Iterator;->hasNext()Z

    move-result v10

    if-eqz v10, :cond_2b

    invoke-interface {v7}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v10

    check-cast v10, Lcom/google/android/gms/measurement/internal/zzjx;

    if-eq v10, v12, :cond_2a

    goto :goto_1f

    :cond_2b
    :goto_1e
    const/4 v5, 0x0

    :goto_1f
    if-eqz v5, :cond_2c

    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    const/4 v7, 0x1

    invoke-virtual {v8, v5, v7}, Lcom/google/android/gms/measurement/internal/zzkf;->x(Lcom/google/android/gms/measurement/internal/zzju;Z)V

    move-object v14, v5

    goto :goto_20

    :cond_2c
    move-object/from16 v14, v23

    :goto_20
    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    iget-object v5, v8, Lmv;->b:Ljava/lang/Object;

    check-cast v5, Lcom/google/android/gms/measurement/internal/zzim;

    invoke-virtual {v8, v14}, Lcom/google/android/gms/measurement/internal/zzkf;->w(Lcom/google/android/gms/measurement/internal/zzju;)V

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v7

    const-string v10, "dma_consent_settings"

    const/4 v15, 0x0

    invoke-interface {v7, v10, v15}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-static {v7}, Lcom/google/android/gms/measurement/internal/zzbb;->b(Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzbb;

    move-result-object v7

    iget v7, v7, Lcom/google/android/gms/measurement/internal/zzbb;->a:I

    const-string v10, "google_analytics_default_allow_ad_personalization_signals"

    const/4 v11, 0x1

    invoke-virtual {v3, v10, v11}, Lcom/google/android/gms/measurement/internal/zzak;->q(Ljava/lang/String;Z)Lcom/google/android/gms/measurement/internal/zzjx;

    move-result-object v10

    if-eq v10, v12, :cond_2d

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v14, v4, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v15, "Default ad personalization consent from Manifest"

    invoke-virtual {v14, v15, v10}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    :cond_2d
    const-string v10, "google_analytics_default_allow_ad_user_data"

    invoke-virtual {v3, v10, v11}, Lcom/google/android/gms/measurement/internal/zzak;->q(Ljava/lang/String;Z)Lcom/google/android/gms/measurement/internal/zzjx;

    move-result-object v10

    if-eq v10, v12, :cond_2f

    const/16 v14, -0xa

    invoke-static {v14, v7}, Lcom/google/android/gms/measurement/internal/zzju;->h(II)Z

    move-result v15

    if-eqz v15, :cond_2f

    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    new-instance v0, Ljava/util/EnumMap;

    invoke-direct {v0, v1}, Ljava/util/EnumMap;-><init>(Ljava/lang/Class;)V

    sget-object v1, Lcom/google/android/gms/measurement/internal/zzju$zza;->d:Lcom/google/android/gms/measurement/internal/zzju$zza;

    invoke-virtual {v0, v1, v10}, Ljava/util/EnumMap;->put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;

    new-instance v1, Lcom/google/android/gms/measurement/internal/zzbb;

    const/4 v15, 0x0

    invoke-direct {v1, v0, v14, v15, v15}, Lcom/google/android/gms/measurement/internal/zzbb;-><init>(Ljava/util/EnumMap;ILjava/lang/Boolean;Ljava/lang/String;)V

    invoke-virtual {v8, v1, v11}, Lcom/google/android/gms/measurement/internal/zzkf;->v(Lcom/google/android/gms/measurement/internal/zzbb;Z)V

    :cond_2e
    :goto_21
    move/from16 v31, v6

    move-object v0, v8

    goto/16 :goto_22

    :cond_2f
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_31

    if-eqz v7, :cond_30

    const/16 v1, 0x1e

    if-ne v7, v1, :cond_31

    :cond_30
    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzbb;

    const/16 v14, -0xa

    const/4 v15, 0x0

    invoke-direct {v0, v14, v15, v15, v15}, Lcom/google/android/gms/measurement/internal/zzbb;-><init>(ILjava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;)V

    const/4 v7, 0x1

    invoke-virtual {v8, v0, v7}, Lcom/google/android/gms/measurement/internal/zzkf;->v(Lcom/google/android/gms/measurement/internal/zzbb;Z)V

    goto :goto_21

    :cond_31
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_33

    if-eqz v0, :cond_33

    iget-object v1, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zzg:Landroid/os/Bundle;

    if-eqz v1, :cond_33

    const/16 v1, 0x1e

    invoke-static {v1, v7}, Lcom/google/android/gms/measurement/internal/zzju;->h(II)Z

    move-result v7

    if-eqz v7, :cond_33

    iget-object v7, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zzg:Landroid/os/Bundle;

    invoke-static {v1, v7}, Lcom/google/android/gms/measurement/internal/zzbb;->a(ILandroid/os/Bundle;)Lcom/google/android/gms/measurement/internal/zzbb;

    move-result-object v1

    iget-object v7, v1, Lcom/google/android/gms/measurement/internal/zzbb;->e:Ljava/util/EnumMap;

    invoke-virtual {v7}, Ljava/util/EnumMap;->values()Ljava/util/Collection;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/Collection;->iterator()Ljava/util/Iterator;

    move-result-object v7

    :cond_32
    invoke-interface {v7}, Ljava/util/Iterator;->hasNext()Z

    move-result v10

    if-eqz v10, :cond_33

    invoke-interface {v7}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v10

    check-cast v10, Lcom/google/android/gms/measurement/internal/zzjx;

    if-eq v10, v12, :cond_32

    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    const/4 v7, 0x1

    invoke-virtual {v8, v1, v7}, Lcom/google/android/gms/measurement/internal/zzkf;->v(Lcom/google/android/gms/measurement/internal/zzbb;Z)V

    :cond_33
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_2e

    if-eqz v0, :cond_2e

    iget-object v1, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zzg:Landroid/os/Bundle;

    if-eqz v1, :cond_2e

    iget-object v1, v9, Lw63;->A:Lcom/google/android/gms/measurement/internal/zzhr;

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzhr;->a()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_2e

    iget-object v1, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zzg:Landroid/os/Bundle;

    invoke-static {v1}, Lcom/google/android/gms/measurement/internal/zzbb;->c(Landroid/os/Bundle;)Ljava/lang/Boolean;

    move-result-object v1

    if-eqz v1, :cond_2e

    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    iget-object v0, v0, Lcom/google/android/gms/internal/measurement/zzdz;->zze:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/Boolean;->toString()Ljava/lang/String;

    move-result-object v30

    iget-object v1, v5, Lcom/google/android/gms/measurement/internal/zzim;->z:Lcom/google/android/gms/common/util/DefaultClock;

    invoke-virtual {v1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v32

    const-string v29, "allow_personalized_ads"

    move-object/from16 v28, v0

    move/from16 v31, v6

    move-object/from16 v27, v8

    invoke-virtual/range {v27 .. v33}, Lcom/google/android/gms/measurement/internal/zzkf;->D(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;ZJ)V

    move-object/from16 v0, v27

    :goto_22
    const-string v1, "google_analytics_tcf_data_enabled"

    invoke-virtual {v3, v1}, Lcom/google/android/gms/measurement/internal/zzak;->s(Ljava/lang/String;)Ljava/lang/Boolean;

    move-result-object v1

    if-nez v1, :cond_34

    const/4 v1, 0x1

    goto :goto_23

    :cond_34
    invoke-virtual {v1}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v1

    :goto_23
    if-eqz v1, :cond_36

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->z:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "TCF client enabled."

    invoke-virtual {v1, v6}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    invoke-virtual {v0}, Lq53;->j()V

    invoke-virtual {v0}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v1

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzhc;->z:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Register tcfPrefChangeListener."

    invoke-virtual {v1, v6}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzkf;->H:Lcom/google/android/gms/measurement/internal/zzks;

    if-nez v1, :cond_35

    new-instance v1, Lu73;

    const/4 v6, 0x2

    invoke-direct {v1, v0, v5, v6}, Lu73;-><init>(Lcom/google/android/gms/measurement/internal/zzkf;Lk73;I)V

    iput-object v1, v0, Lcom/google/android/gms/measurement/internal/zzkf;->I:Lu73;

    new-instance v1, Lcom/google/android/gms/measurement/internal/zzks;

    invoke-direct {v1}, Ljava/lang/Object;-><init>()V

    iput-object v0, v1, Lcom/google/android/gms/measurement/internal/zzks;->a:Lcom/google/android/gms/measurement/internal/zzkf;

    iput-object v1, v0, Lcom/google/android/gms/measurement/internal/zzkf;->H:Lcom/google/android/gms/measurement/internal/zzks;

    :cond_35
    invoke-virtual {v0}, Lmv;->h()Lw63;

    move-result-object v1

    invoke-virtual {v1}, Lw63;->r()Landroid/content/SharedPreferences;

    move-result-object v1

    iget-object v6, v0, Lcom/google/android/gms/measurement/internal/zzkf;->H:Lcom/google/android/gms/measurement/internal/zzks;

    invoke-interface {v1, v6}, Landroid/content/SharedPreferences;->registerOnSharedPreferenceChangeListener(Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;)V

    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzkf;->I()V

    :cond_36
    invoke-virtual {v13}, Lcom/google/android/gms/measurement/internal/zzhp;->a()J

    move-result-wide v6

    const-wide/16 v17, 0x0

    cmp-long v1, v6, v17

    if-nez v1, :cond_37

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Persisting first open"

    invoke-static/range {v37 .. v38}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v7

    invoke-virtual {v1, v6, v7}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    move-wide/from16 v6, v37

    invoke-virtual {v13, v6, v7}, Lcom/google/android/gms/measurement/internal/zzhp;->b(J)V

    goto :goto_24

    :cond_37
    move-wide/from16 v6, v37

    :goto_24
    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzkf;->E:Lcom/google/android/gms/measurement/internal/zzw;

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzw;->b()Z

    move-result v8

    if-eqz v8, :cond_38

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzw;->c()Z

    move-result v8

    if-eqz v8, :cond_38

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzw;->a:Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzim;->t:Lw63;

    invoke-static {v1}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    iget-object v1, v1, Lw63;->K:Lcom/google/android/gms/measurement/internal/zzhr;

    const/4 v15, 0x0

    invoke-virtual {v1, v15}, Lcom/google/android/gms/measurement/internal/zzhr;->b(Ljava/lang/String;)V

    :cond_38
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->g()Z

    move-result v1

    if-nez v1, :cond_3e

    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->f()Z

    move-result v1

    if-eqz v1, :cond_3d

    invoke-static/range {v36 .. v36}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    const-string v1, "android.permission.INTERNET"

    move-object/from16 v8, v36

    invoke-virtual {v8, v1}, Lcom/google/android/gms/measurement/internal/zzqd;->n0(Ljava/lang/String;)Z

    move-result v1

    if-nez v1, :cond_39

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "App is missing INTERNET permission"

    invoke-virtual {v1, v2}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_39
    const-string v1, "android.permission.ACCESS_NETWORK_STATE"

    invoke-virtual {v8, v1}, Lcom/google/android/gms/measurement/internal/zzqd;->n0(Ljava/lang/String;)Z

    move-result v1

    if-nez v1, :cond_3a

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "App is missing ACCESS_NETWORK_STATE permission"

    invoke-virtual {v1, v2}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_3a
    invoke-static/range {v22 .. v22}, Lcom/google/android/gms/common/wrappers/Wrappers;->a(Landroid/content/Context;)Lcom/google/android/gms/common/wrappers/PackageManagerWrapper;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/common/wrappers/PackageManagerWrapper;->c()Z

    move-result v1

    if-nez v1, :cond_3c

    invoke-virtual {v3}, Lcom/google/android/gms/measurement/internal/zzak;->w()Z

    move-result v1

    if-nez v1, :cond_3c

    invoke-static/range {v22 .. v22}, Lcom/google/android/gms/measurement/internal/zzqd;->S(Landroid/content/Context;)Z

    move-result v1

    if-nez v1, :cond_3b

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "AppMeasurementReceiver not registered/enabled"

    invoke-virtual {v1, v2}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_3b
    invoke-static/range {v22 .. v22}, Lcom/google/android/gms/measurement/internal/zzqd;->g0(Landroid/content/Context;)Z

    move-result v1

    if-nez v1, :cond_3c

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "AppMeasurementService not registered/enabled"

    invoke-virtual {v1, v2}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_3c
    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "Uploading is not possible. App measurement disabled"

    invoke-virtual {v1, v2}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :goto_25
    move-object/from16 v17, v5

    move-object/from16 v1, v34

    goto/16 :goto_2d

    :cond_3d
    move-object/from16 v8, v36

    goto :goto_25

    :cond_3e
    move-object/from16 v8, v36

    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_40

    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v1

    invoke-virtual {v1}, Lp63;->p()V

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_3f

    goto :goto_26

    :cond_3f
    move-object/from16 v17, v5

    move-object/from16 v6, v26

    move-object/from16 v1, v34

    goto/16 :goto_29

    :cond_40
    :goto_26
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->o()V

    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v10

    const-string v11, "gmp_app_id"

    const/4 v15, 0x0

    invoke-interface {v10, v11, v15}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v12

    invoke-virtual {v12}, Lp63;->p()V

    iget-object v12, v12, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v14

    move-object/from16 v17, v5

    move-object/from16 v5, v35

    invoke-interface {v14, v5, v15}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v14

    invoke-static {v1, v10, v12, v14}, Lcom/google/android/gms/measurement/internal/zzqd;->X(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_43

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v1, v4, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v10, "Rechecking which service to use due to a GMP App Id change"

    invoke-virtual {v1, v10}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v1

    const-string v10, "measurement_enabled"

    invoke-interface {v1, v10}, Landroid/content/SharedPreferences;->contains(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_41

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v1

    const/4 v12, 0x1

    invoke-interface {v1, v10, v12}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v1

    invoke-static {v1}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v1

    goto :goto_27

    :cond_41
    const/4 v1, 0x0

    :goto_27
    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v12

    invoke-interface {v12}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v12

    invoke-interface {v12}, Landroid/content/SharedPreferences$Editor;->clear()Landroid/content/SharedPreferences$Editor;

    invoke-interface {v12}, Landroid/content/SharedPreferences$Editor;->apply()V

    if-eqz v1, :cond_42

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v12

    invoke-interface {v12}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v12

    invoke-virtual {v1}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v1

    invoke-interface {v12, v10, v1}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v12}, Landroid/content/SharedPreferences$Editor;->apply()V

    :cond_42
    invoke-virtual/range {v34 .. v34}, Lcom/google/android/gms/measurement/internal/zzim;->k()Lcom/google/android/gms/measurement/internal/zzgu;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzgu;->t()V

    move-object/from16 v1, v34

    iget-object v10, v1, Lcom/google/android/gms/measurement/internal/zzim;->G:Lcom/google/android/gms/measurement/internal/zzmp;

    invoke-virtual {v10}, Lcom/google/android/gms/measurement/internal/zzmp;->x()V

    iget-object v10, v1, Lcom/google/android/gms/measurement/internal/zzim;->G:Lcom/google/android/gms/measurement/internal/zzmp;

    invoke-virtual {v10}, Lcom/google/android/gms/measurement/internal/zzmp;->w()V

    invoke-virtual {v13, v6, v7}, Lcom/google/android/gms/measurement/internal/zzhp;->b(J)V

    move-object/from16 v6, v26

    const/4 v15, 0x0

    invoke-virtual {v6, v15}, Lcom/google/android/gms/measurement/internal/zzhr;->b(Ljava/lang/String;)V

    goto :goto_28

    :cond_43
    move-object/from16 v6, v26

    move-object/from16 v1, v34

    :goto_28
    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v7

    invoke-virtual {v7}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v10

    invoke-interface {v10}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v10

    invoke-interface {v10, v11, v7}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v10}, Landroid/content/SharedPreferences$Editor;->apply()V

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v7

    invoke-virtual {v7}, Lp63;->p()V

    iget-object v7, v7, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    invoke-virtual {v9}, Lmv;->j()V

    invoke-virtual {v9}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v10

    invoke-interface {v10}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v10

    invoke-interface {v10, v5, v7}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v10}, Landroid/content/SharedPreferences$Editor;->apply()V

    :goto_29
    invoke-virtual {v9}, Lw63;->u()Lcom/google/android/gms/measurement/internal/zzju;

    move-result-object v5

    invoke-virtual {v5, v2}, Lcom/google/android/gms/measurement/internal/zzju;->i(Lcom/google/android/gms/measurement/internal/zzju$zza;)Z

    move-result v2

    if-nez v2, :cond_44

    const/4 v15, 0x0

    invoke-virtual {v6, v15}, Lcom/google/android/gms/measurement/internal/zzhr;->b(Ljava/lang/String;)V

    :cond_44
    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    invoke-virtual {v6}, Lcom/google/android/gms/measurement/internal/zzhr;->a()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lcom/google/android/gms/measurement/internal/zzkf;->L(Ljava/lang/String;)V

    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    :try_start_6
    iget-object v2, v8, Lmv;->b:Ljava/lang/Object;

    check-cast v2, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v2, v2, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getClassLoader()Ljava/lang/ClassLoader;

    move-result-object v2

    const-string v5, "com.google.firebase.remoteconfig.FirebaseRemoteConfig"

    invoke-virtual {v2, v5}, Ljava/lang/ClassLoader;->loadClass(Ljava/lang/String;)Ljava/lang/Class;
    :try_end_6
    .catch Ljava/lang/ClassNotFoundException; {:try_start_6 .. :try_end_6} :catch_6

    goto :goto_2a

    :catch_6
    invoke-virtual/range {v24 .. v24}, Lcom/google/android/gms/measurement/internal/zzhr;->a()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v2

    if-nez v2, :cond_45

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v2, v4, Lcom/google/android/gms/measurement/internal/zzhc;->v:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v5, "Remote config removed with active feature rollouts"

    invoke-virtual {v2, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    move-object/from16 v2, v24

    const/4 v15, 0x0

    invoke-virtual {v2, v15}, Lcom/google/android/gms/measurement/internal/zzhr;->b(Ljava/lang/String;)V

    :cond_45
    :goto_2a
    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v2

    invoke-virtual {v2}, Lcom/google/android/gms/measurement/internal/zzgr;->s()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v2

    if-eqz v2, :cond_46

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->j()Lcom/google/android/gms/measurement/internal/zzgr;

    move-result-object v2

    invoke-virtual {v2}, Lp63;->p()V

    iget-object v2, v2, Lcom/google/android/gms/measurement/internal/zzgr;->A:Ljava/lang/String;

    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v2

    if-nez v2, :cond_4b

    :cond_46
    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->f()Z

    move-result v2

    iget-object v5, v9, Lw63;->d:Landroid/content/SharedPreferences;

    if-nez v5, :cond_47

    move/from16 v11, v31

    goto :goto_2b

    :cond_47
    const-string v6, "deferred_analytics_collection"

    invoke-interface {v5, v6}, Landroid/content/SharedPreferences;->contains(Ljava/lang/String;)Z

    move-result v11

    :goto_2b
    if-nez v11, :cond_49

    const-string v5, "firebase_analytics_collection_deactivated"

    invoke-virtual {v3, v5}, Lcom/google/android/gms/measurement/internal/zzak;->s(Ljava/lang/String;)Ljava/lang/Boolean;

    move-result-object v5

    if-eqz v5, :cond_48

    invoke-virtual {v5}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v5

    if-eqz v5, :cond_48

    goto :goto_2c

    :cond_48
    xor-int/lit8 v5, v2, 0x1

    invoke-virtual {v9, v5}, Lw63;->q(Z)V

    :cond_49
    :goto_2c
    if-eqz v2, :cond_4a

    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzkf;->F()V

    :cond_4a
    iget-object v2, v1, Lcom/google/android/gms/measurement/internal/zzim;->w:Lcom/google/android/gms/measurement/internal/zzoi;

    invoke-static {v2}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    iget-object v2, v2, Lcom/google/android/gms/measurement/internal/zzoi;->f:Lc13;

    invoke-virtual {v2}, Lc13;->b()V

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->n()Lcom/google/android/gms/measurement/internal/zzmp;

    move-result-object v2

    new-instance v5, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-direct {v5}, Ljava/util/concurrent/atomic/AtomicReference;-><init>()V

    invoke-virtual {v2, v5}, Lcom/google/android/gms/measurement/internal/zzmp;->u(Ljava/util/concurrent/atomic/AtomicReference;)V

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->n()Lcom/google/android/gms/measurement/internal/zzmp;

    move-result-object v2

    iget-object v5, v9, Lw63;->M:Lcom/google/android/gms/measurement/internal/zzhq;

    invoke-virtual {v5}, Lcom/google/android/gms/measurement/internal/zzhq;->a()Landroid/os/Bundle;

    move-result-object v5

    invoke-virtual {v2, v5}, Lcom/google/android/gms/measurement/internal/zzmp;->r(Landroid/os/Bundle;)V

    :cond_4b
    :goto_2d
    invoke-static {}, Lcom/google/android/gms/internal/measurement/zzpf;->zza()Z

    move-result v2

    if-eqz v2, :cond_4e

    sget-object v2, Lcom/google/android/gms/measurement/internal/zzbl;->V0:Lcom/google/android/gms/measurement/internal/zzgi;

    const/4 v15, 0x0

    invoke-virtual {v3, v15, v2}, Lcom/google/android/gms/measurement/internal/zzak;->t(Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzgi;)Z

    move-result v2

    if-eqz v2, :cond_4e

    invoke-static {v8}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    invoke-virtual {v8}, Lmv;->j()V

    invoke-virtual {v8}, Lcom/google/android/gms/measurement/internal/zzqd;->s0()J

    move-result-wide v2

    cmp-long v2, v2, v19

    if-nez v2, :cond_4e

    sget-object v2, Lcom/google/android/gms/measurement/internal/zzbl;->v0:Lcom/google/android/gms/measurement/internal/zzgi;

    invoke-virtual {v2, v15}, Lcom/google/android/gms/measurement/internal/zzgi;->a(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v2

    int-to-long v2, v2

    const-wide/16 v5, 0x3e8

    mul-long/2addr v2, v5

    new-instance v5, Ljava/util/Random;

    invoke-direct {v5}, Ljava/util/Random;-><init>()V

    const/16 v6, 0x1388

    invoke-virtual {v5, v6}, Ljava/util/Random;->nextInt(I)I

    move-result v5

    int-to-long v5, v5

    add-long/2addr v2, v5

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzim;->z:Lcom/google/android/gms/common/util/DefaultClock;

    invoke-virtual {v1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v5

    sub-long/2addr v2, v5

    const-wide/16 v5, 0x1f4

    invoke-static {v5, v6, v2, v3}, Ljava/lang/Math;->max(JJ)J

    move-result-wide v1

    cmp-long v3, v1, v5

    if-lez v3, :cond_4c

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v3, v4, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v4, "Waiting to fetch trigger URIs until some time after boot. Delay in millis"

    invoke-static {v1, v2}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v5

    invoke-virtual {v3, v4, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    :cond_4c
    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    invoke-virtual {v0}, Lq53;->j()V

    iget-object v3, v0, Lcom/google/android/gms/measurement/internal/zzkf;->y:Lu73;

    if-nez v3, :cond_4d

    new-instance v3, Lu73;

    move-object/from16 v5, v17

    const/4 v6, 0x0

    invoke-direct {v3, v0, v5, v6}, Lu73;-><init>(Lcom/google/android/gms/measurement/internal/zzkf;Lk73;I)V

    iput-object v3, v0, Lcom/google/android/gms/measurement/internal/zzkf;->y:Lu73;

    :cond_4d
    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzkf;->y:Lu73;

    invoke-virtual {v0, v1, v2}, Lk33;->b(J)V

    :cond_4e
    iget-object v0, v9, Lw63;->C:Lcom/google/android/gms/measurement/internal/zzhn;

    const/4 v9, 0x1

    invoke-virtual {v0, v9}, Lcom/google/android/gms/measurement/internal/zzhn;->a(Z)V

    return-void

    :cond_4f
    invoke-static/range {v25 .. v25}, Lvc;->l(Ljava/lang/String;)V

    return-void

    :cond_50
    move-object/from16 v25, v11

    invoke-static/range {v25 .. v25}, Lvc;->l(Ljava/lang/String;)V

    return-void

    :cond_51
    move-object/from16 v25, v11

    invoke-static/range {v25 .. v25}, Lvc;->l(Ljava/lang/String;)V

    return-void

    :cond_52
    move-object/from16 v25, v11

    invoke-static/range {v25 .. v25}, Lvc;->l(Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 9

    iget v0, p0, Lf53;->a:I

    const-wide/16 v1, 0x0

    const/4 v3, 0x4

    const/4 v4, 0x1

    const/4 v5, 0x0

    const/4 v6, 0x0

    packed-switch v0, :pswitch_data_0

    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Lb23;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Landroid/os/Bundle;

    const-string v1, "HsdpClientImpl"

    :try_start_0
    iget-object v2, v0, Lb23;->b:Lue1;

    iget-object v2, v2, Lue1;->k:Ljava/lang/Object;

    check-cast v2, Landroid/os/IInterface;

    check-cast v2, Lq63;

    if-nez v2, :cond_0

    goto :goto_2

    :cond_0
    iget-object v0, v0, Lb23;->d:Lf13;

    check-cast v2, Lf63;

    invoke-virtual {v2}, Lcom/google/android/gms/internal/playcore_hsdp/zza;->zza()Landroid/os/Parcel;

    move-result-object v4

    invoke-static {v4, p0}, Lcom/google/android/gms/internal/playcore_hsdp/zzc;->zzc(Landroid/os/Parcel;Landroid/os/Parcelable;)V

    invoke-static {v4, v0}, Lcom/google/android/gms/internal/playcore_hsdp/zzc;->zzd(Landroid/os/Parcel;Landroid/os/IInterface;)V

    invoke-virtual {v2, v3, v4}, Lcom/google/android/gms/internal/playcore_hsdp/zza;->zzb(ILandroid/os/Parcel;)V
    :try_end_0
    .catch Landroid/os/DeadObjectException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Landroid/os/RemoteException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_2

    :catch_0
    move-exception v0

    move-object p0, v0

    goto :goto_0

    :catch_1
    move-exception v0

    move-object p0, v0

    goto :goto_1

    :goto_0
    const-string v0, "Failed to call hsdpService.endSession"

    invoke-static {v1, v0, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    goto :goto_2

    :goto_1
    const-string v0, "hsdpService is dead"

    invoke-static {v1, v0, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    :goto_2
    return-void

    :pswitch_0
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    move-object v1, v0

    check-cast v1, Lae0;

    iget-object v0, v1, Lae0;->e:Ljava/lang/Object;

    move-object v2, v0

    check-cast v2, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-static {}, Ljava/lang/Thread;->currentThread()Ljava/lang/Thread;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/util/concurrent/atomic/AtomicReference;->getAndSet(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Thread;

    if-nez v0, :cond_1

    goto :goto_3

    :cond_1
    move v4, v5

    :goto_3
    invoke-static {v4}, Lcom/google/android/gms/common/internal/Preconditions;->j(Z)V

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Ljava/lang/Runnable;

    :try_start_1
    invoke-interface {p0}, Ljava/lang/Runnable;->run()V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    invoke-virtual {v2, v6}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V

    invoke-virtual {v1}, Lae0;->i()V

    return-void

    :catchall_0
    move-exception v0

    move-object p0, v0

    :try_start_2
    invoke-virtual {v2, v6}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V

    invoke-virtual {v1}, Lae0;->i()V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    goto :goto_4

    :catchall_1
    move-exception v0

    invoke-virtual {p0, v0}, Ljava/lang/Throwable;->addSuppressed(Ljava/lang/Throwable;)V

    :goto_4
    throw p0

    :pswitch_1
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzpk;

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzpk;->d0()V

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Ljava/lang/Runnable;

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzpk;->zzl()Lcom/google/android/gms/measurement/internal/zzij;

    move-result-object v1

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzij;->j()V

    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzpk;->B:Ljava/util/ArrayList;

    if-nez v1, :cond_2

    new-instance v1, Ljava/util/ArrayList;

    invoke-direct {v1}, Ljava/util/ArrayList;-><init>()V

    iput-object v1, v0, Lcom/google/android/gms/measurement/internal/zzpk;->B:Ljava/util/ArrayList;

    :cond_2
    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzpk;->B:Ljava/util/ArrayList;

    invoke-virtual {v1, p0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzpk;->f0()V

    return-void

    :pswitch_2
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zznu;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zznu;->c:Lcom/google/android/gms/measurement/internal/zzmp;

    iput-object v6, v0, Lcom/google/android/gms/measurement/internal/zzmp;->e:Lcom/google/android/gms/measurement/internal/zzgk;

    iget-object v1, v0, Lmv;->b:Ljava/lang/Object;

    check-cast v1, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzim;->s:Lcom/google/android/gms/measurement/internal/zzak;

    sget-object v2, Lcom/google/android/gms/measurement/internal/zzbl;->o1:Lcom/google/android/gms/measurement/internal/zzgi;

    invoke-virtual {v1, v6, v2}, Lcom/google/android/gms/measurement/internal/zzak;->t(Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzgi;)Z

    move-result v1

    if-eqz v1, :cond_3

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/common/ConnectionResult;

    iget p0, p0, Lcom/google/android/gms/common/ConnectionResult;->b:I

    const/16 v1, 0x1e61

    if-ne p0, v1, :cond_3

    goto :goto_5

    :cond_3
    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzmp;->B()V

    :goto_5
    return-void

    :pswitch_3
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zznu;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zznu;->c:Lcom/google/android/gms/measurement/internal/zzmp;

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Landroid/content/ComponentName;

    invoke-virtual {v0}, Lq53;->j()V

    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzmp;->e:Lcom/google/android/gms/measurement/internal/zzgk;

    if-eqz v1, :cond_4

    iput-object v6, v0, Lcom/google/android/gms/measurement/internal/zzmp;->e:Lcom/google/android/gms/measurement/internal/zzgk;

    invoke-virtual {v0}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v1

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "Disconnected from device MeasurementService"

    invoke-virtual {v1, v2, p0}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    invoke-virtual {v0}, Lq53;->j()V

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzmp;->w()V

    :cond_4
    return-void

    :pswitch_4
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    move-object v1, v0

    check-cast v1, Lcom/google/android/gms/measurement/internal/zzmp;

    iget-object v2, v1, Lcom/google/android/gms/measurement/internal/zzmp;->e:Lcom/google/android/gms/measurement/internal/zzgk;

    if-nez v2, :cond_5

    invoke-virtual {v1}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object p0

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v0, "Failed to send current screen to service"

    invoke-virtual {p0, v0}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    goto :goto_8

    :cond_5
    :try_start_3
    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzmh;

    if-nez p0, :cond_6

    iget-object p0, v1, Lmv;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v7

    const-wide/16 v3, 0x0

    const/4 v5, 0x0

    const/4 v6, 0x0

    invoke-interface/range {v2 .. v7}, Lcom/google/android/gms/measurement/internal/zzgk;->J(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    goto :goto_6

    :catch_2
    move-exception v0

    move-object p0, v0

    goto :goto_7

    :cond_6
    iget-wide v3, p0, Lcom/google/android/gms/measurement/internal/zzmh;->c:J

    iget-object v5, p0, Lcom/google/android/gms/measurement/internal/zzmh;->a:Ljava/lang/String;

    iget-object v6, p0, Lcom/google/android/gms/measurement/internal/zzmh;->b:Ljava/lang/String;

    iget-object p0, v1, Lmv;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzim;->a:Landroid/content/Context;

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v7

    invoke-interface/range {v2 .. v7}, Lcom/google/android/gms/measurement/internal/zzgk;->J(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    :goto_6
    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzmp;->C()V
    :try_end_3
    .catch Landroid/os/RemoteException; {:try_start_3 .. :try_end_3} :catch_2

    goto :goto_8

    :goto_7
    invoke-virtual {v1}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v1, "Failed to send current screen to the service"

    invoke-virtual {v0, v1, p0}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    :goto_8
    return-void

    :pswitch_5
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzkf;

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Ljava/lang/Boolean;

    invoke-virtual {v0, p0, v4}, Lcom/google/android/gms/measurement/internal/zzkf;->z(Ljava/lang/Boolean;Z)V

    return-void

    :pswitch_6
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzkf;

    iget-object v1, v0, Lmv;->b:Ljava/lang/Object;

    check-cast v1, Lcom/google/android/gms/measurement/internal/zzim;

    invoke-virtual {v0}, Lmv;->h()Lw63;

    move-result-object v2

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzbb;

    invoke-virtual {v2}, Lmv;->j()V

    invoke-virtual {v2}, Lmv;->j()V

    invoke-virtual {v2}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v4

    const-string v7, "dma_consent_settings"

    invoke-interface {v4, v7, v6}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Lcom/google/android/gms/measurement/internal/zzbb;->b(Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzbb;

    move-result-object v4

    iget v6, p0, Lcom/google/android/gms/measurement/internal/zzbb;->a:I

    iget v4, v4, Lcom/google/android/gms/measurement/internal/zzbb;->a:I

    invoke-static {v6, v4}, Lcom/google/android/gms/measurement/internal/zzju;->h(II)Z

    move-result v4

    if-eqz v4, :cond_9

    invoke-virtual {v2}, Lw63;->s()Landroid/content/SharedPreferences;

    move-result-object v2

    invoke-interface {v2}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v2

    iget-object v4, p0, Lcom/google/android/gms/measurement/internal/zzbb;->b:Ljava/lang/String;

    invoke-interface {v2, v7, v4}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v2}, Landroid/content/SharedPreferences$Editor;->apply()V

    invoke-virtual {v0}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->A:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "Setting DMA consent(FE)"

    invoke-virtual {v0, v2, p0}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->n()Lcom/google/android/gms/measurement/internal/zzmp;

    move-result-object p0

    invoke-virtual {p0}, Lq53;->j()V

    invoke-virtual {p0}, Lp63;->p()V

    invoke-virtual {p0}, Lcom/google/android/gms/measurement/internal/zzmp;->A()Z

    move-result v0

    if-nez v0, :cond_7

    goto :goto_9

    :cond_7
    invoke-virtual {p0}, Lmv;->i()Lcom/google/android/gms/measurement/internal/zzqd;

    move-result-object p0

    invoke-virtual {p0}, Lcom/google/android/gms/measurement/internal/zzqd;->p0()I

    move-result p0

    const v0, 0x3ae30

    if-lt p0, v0, :cond_8

    :goto_9
    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->n()Lcom/google/android/gms/measurement/internal/zzmp;

    move-result-object p0

    invoke-virtual {p0}, Lq53;->j()V

    invoke-virtual {p0}, Lp63;->p()V

    new-instance v0, Lcom/google/android/gms/measurement/internal/zzmr;

    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    iput-object p0, v0, Lcom/google/android/gms/measurement/internal/zzmr;->a:Lcom/google/android/gms/measurement/internal/zzmp;

    invoke-virtual {p0, v0}, Lcom/google/android/gms/measurement/internal/zzmp;->v(Ljava/lang/Runnable;)V

    goto :goto_a

    :cond_8
    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzim;->n()Lcom/google/android/gms/measurement/internal/zzmp;

    move-result-object p0

    invoke-virtual {p0}, Lq53;->j()V

    invoke-virtual {p0}, Lp63;->p()V

    invoke-virtual {p0}, Lcom/google/android/gms/measurement/internal/zzmp;->z()Z

    move-result v0

    if-eqz v0, :cond_a

    invoke-virtual {p0, v5}, Lcom/google/android/gms/measurement/internal/zzmp;->D(Z)Lcom/google/android/gms/measurement/internal/zzq;

    move-result-object v0

    new-instance v1, Lt83;

    invoke-direct {v1, p0, v0, v3}, Lt83;-><init>(Lcom/google/android/gms/measurement/internal/zzmp;Lcom/google/android/gms/measurement/internal/zzq;I)V

    invoke-virtual {p0, v1}, Lcom/google/android/gms/measurement/internal/zzmp;->v(Ljava/lang/Runnable;)V

    goto :goto_a

    :cond_9
    invoke-virtual {v0}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object p0

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzhc;->y:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v0, "Lower precedence consent source ignored, proposed source"

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-virtual {p0, v0, v1}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    :cond_a
    :goto_a
    return-void

    :pswitch_7
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/internal/measurement/zzdq;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzkf;

    invoke-virtual {p0}, Lq53;->n()Lcom/google/android/gms/measurement/internal/zzoi;

    move-result-object v3

    iget-object p0, p0, Lmv;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzim;

    invoke-virtual {v3}, Lmv;->h()Lw63;

    move-result-object v4

    invoke-virtual {v4}, Lw63;->u()Lcom/google/android/gms/measurement/internal/zzju;

    move-result-object v4

    sget-object v5, Lcom/google/android/gms/measurement/internal/zzju$zza;->c:Lcom/google/android/gms/measurement/internal/zzju$zza;

    invoke-virtual {v4, v5}, Lcom/google/android/gms/measurement/internal/zzju;->i(Lcom/google/android/gms/measurement/internal/zzju$zza;)Z

    move-result v4

    if-nez v4, :cond_c

    invoke-virtual {v3}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v1

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzhc;->x:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "Analytics storage consent denied; will not get session id"

    invoke-virtual {v1, v2}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V

    :cond_b
    :goto_b
    move-object v1, v6

    goto :goto_c

    :cond_c
    invoke-virtual {v3}, Lmv;->h()Lw63;

    move-result-object v4

    iget-object v5, v3, Lmv;->b:Ljava/lang/Object;

    check-cast v5, Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzim;->z:Lcom/google/android/gms/common/util/DefaultClock;

    invoke-virtual {v5}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v7

    invoke-virtual {v4, v7, v8}, Lw63;->o(J)Z

    move-result v4

    if-nez v4, :cond_b

    invoke-virtual {v3}, Lmv;->h()Lw63;

    move-result-object v4

    iget-object v4, v4, Lw63;->E:Lcom/google/android/gms/measurement/internal/zzhp;

    invoke-virtual {v4}, Lcom/google/android/gms/measurement/internal/zzhp;->a()J

    move-result-wide v4

    cmp-long v1, v4, v1

    if-nez v1, :cond_d

    goto :goto_b

    :cond_d
    invoke-virtual {v3}, Lmv;->h()Lw63;

    move-result-object v1

    iget-object v1, v1, Lw63;->E:Lcom/google/android/gms/measurement/internal/zzhp;

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzhp;->a()J

    move-result-wide v1

    invoke-static {v1, v2}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v1

    :goto_c
    if-eqz v1, :cond_e

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzim;->x:Lcom/google/android/gms/measurement/internal/zzqd;

    invoke-static {p0}, Lcom/google/android/gms/measurement/internal/zzim;->b(Lmv;)V

    invoke-virtual {v1}, Ljava/lang/Long;->longValue()J

    move-result-wide v1

    invoke-virtual {p0, v0, v1, v2}, Lcom/google/android/gms/measurement/internal/zzqd;->F(Lcom/google/android/gms/internal/measurement/zzdq;J)V

    goto :goto_d

    :cond_e
    :try_start_4
    invoke-interface {v0, v6}, Lcom/google/android/gms/internal/measurement/zzdq;->zza(Landroid/os/Bundle;)V
    :try_end_4
    .catch Landroid/os/RemoteException; {:try_start_4 .. :try_end_4} :catch_3

    goto :goto_d

    :catch_3
    move-exception v0

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzim;->u:Lcom/google/android/gms/measurement/internal/zzhc;

    invoke-static {p0}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object p0, p0, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v1, "getSessionId failed with exception"

    invoke-virtual {p0, v1, v0}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    :goto_d
    return-void

    :pswitch_8
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    move-object v1, v0

    check-cast v1, Lhl2;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/tasks/TaskCompletionSource;

    iget-object v0, v1, Lhl2;->b:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicInteger;->decrementAndGet()I

    move-result v0

    if-ltz v0, :cond_f

    move v2, v4

    goto :goto_e

    :cond_f
    move v2, v5

    :goto_e
    invoke-static {v2}, Lcom/google/android/gms/common/internal/Preconditions;->j(Z)V

    if-nez v0, :cond_10

    monitor-enter v1

    :try_start_5
    sput-boolean v4, Lhl2;->i:Z

    iget-object v0, v1, Lhl2;->d:Lg83;

    invoke-interface {v0}, Lg83;->zzc()V
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_2

    monitor-exit v1

    iget-object v0, v1, Lhl2;->c:Ljava/util/concurrent/atomic/AtomicBoolean;

    invoke-virtual {v0, v5}, Ljava/util/concurrent/atomic/AtomicBoolean;->set(Z)V

    goto :goto_f

    :catchall_2
    move-exception v0

    move-object p0, v0

    :try_start_6
    monitor-exit v1
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_2

    throw p0

    :cond_10
    :goto_f
    invoke-static {}, Lcom/google/android/gms/internal/mlkit_common/zzrr;->zza()V

    invoke-virtual {p0, v6}, Lcom/google/android/gms/tasks/TaskCompletionSource;->setResult(Ljava/lang/Object;)V

    return-void

    :pswitch_9
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/AppMeasurementDynamiteService;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/AppMeasurementDynamiteService;->a:Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzim;->B:Lcom/google/android/gms/measurement/internal/zzkf;

    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzim;->d(Lp63;)V

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lza0;

    invoke-virtual {v0}, Lq53;->j()V

    invoke-virtual {v0}, Lp63;->p()V

    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzkf;->e:Lcom/google/android/gms/measurement/internal/zzkb;

    if-eq p0, v1, :cond_12

    if-nez v1, :cond_11

    goto :goto_10

    :cond_11
    move v4, v5

    :goto_10
    const-string v1, "EventInterceptor already set."

    invoke-static {v4, v1}, Lcom/google/android/gms/common/internal/Preconditions;->k(ZLjava/lang/String;)V

    :cond_12
    iput-object p0, v0, Lcom/google/android/gms/measurement/internal/zzkf;->e:Lcom/google/android/gms/measurement/internal/zzkb;

    return-void

    :pswitch_a
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Lq43;

    iget-object v0, v0, Lq43;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/ads/internal/overlay/zzm;

    iget-object v0, v0, Lcom/google/android/gms/ads/internal/overlay/zzm;->a:Landroid/app/Activity;

    invoke-virtual {v0}, Landroid/app/Activity;->getWindow()Landroid/view/Window;

    move-result-object v0

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Landroid/graphics/drawable/BitmapDrawable;

    invoke-virtual {v0, p0}, Landroid/view/Window;->setBackgroundDrawable(Landroid/graphics/drawable/Drawable;)V

    return-void

    :pswitch_b
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzir;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzir;->a:Lcom/google/android/gms/measurement/internal/zzpk;

    invoke-virtual {v0}, Lcom/google/android/gms/measurement/internal/zzpk;->d0()V

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzai;

    iget-object v1, p0, Lcom/google/android/gms/measurement/internal/zzai;->c:Lcom/google/android/gms/measurement/internal/zzpy;

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzpy;->zza()Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_13

    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    iget-object v1, p0, Lcom/google/android/gms/measurement/internal/zzai;->a:Ljava/lang/String;

    invoke-static {v1}, Lcom/google/android/gms/common/internal/Preconditions;->h(Ljava/lang/Object;)V

    invoke-virtual {v0, v1}, Lcom/google/android/gms/measurement/internal/zzpk;->O(Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzq;

    move-result-object v1

    if-eqz v1, :cond_14

    invoke-virtual {v0, p0, v1}, Lcom/google/android/gms/measurement/internal/zzpk;->n(Lcom/google/android/gms/measurement/internal/zzai;Lcom/google/android/gms/measurement/internal/zzq;)V

    goto :goto_11

    :cond_13
    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    iget-object v1, p0, Lcom/google/android/gms/measurement/internal/zzai;->a:Ljava/lang/String;

    invoke-static {v1}, Lcom/google/android/gms/common/internal/Preconditions;->h(Ljava/lang/Object;)V

    invoke-virtual {v0, v1}, Lcom/google/android/gms/measurement/internal/zzpk;->O(Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzq;

    move-result-object v1

    if-eqz v1, :cond_14

    invoke-virtual {v0, p0, v1}, Lcom/google/android/gms/measurement/internal/zzpk;->I(Lcom/google/android/gms/measurement/internal/zzai;Lcom/google/android/gms/measurement/internal/zzq;)V

    :cond_14
    :goto_11
    return-void

    :pswitch_c
    invoke-direct {p0}, Lf53;->a()V

    return-void

    :pswitch_d
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    move-object v3, v0

    check-cast v3, Lcom/google/android/gms/ads/internal/util/zzj;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Landroid/content/Context;

    const-string v0, "admob"

    invoke-virtual {p0, v0, v5}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object p0

    invoke-interface {p0}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v0

    :try_start_7
    iget-object v4, v3, Lcom/google/android/gms/ads/internal/util/zzj;->a:Ljava/lang/Object;

    monitor-enter v4
    :try_end_7
    .catchall {:try_start_7 .. :try_end_7} :catchall_4

    :try_start_8
    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    iput-object v0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->g:Landroid/content/SharedPreferences$Editor;

    invoke-static {}, Landroid/security/NetworkSecurityPolicy;->getInstance()Landroid/security/NetworkSecurityPolicy;

    move-result-object p0

    invoke-virtual {p0}, Landroid/security/NetworkSecurityPolicy;->isCleartextTrafficPermitted()Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "use_https"

    iget-boolean v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->h:Z

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    iput-boolean p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->h:Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "content_url_opted_out"

    iget-boolean v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->u:Z

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    iput-boolean p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->u:Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "content_url_hashes"

    iget-object v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->i:Ljava/lang/String;

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->i:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "gad_idless"

    iget-boolean v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->k:Z

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    iput-boolean p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->k:Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "content_vertical_opted_out"

    iget-boolean v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->v:Z

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    iput-boolean p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->v:Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "content_vertical_hashes"

    iget-object v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->j:Ljava/lang/String;

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->j:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "version_code"

    iget v5, v3, Lcom/google/android/gms/ads/internal/util/zzj;->r:I

    invoke-interface {p0, v0, v5}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->r:I

    sget-object p0, Lcom/google/android/gms/internal/ads/zzbkz;->zzg:Lcom/google/android/gms/internal/ads/zzbkq;

    invoke-virtual {p0}, Lcom/google/android/gms/internal/ads/zzbkq;->zze()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Boolean;

    invoke-virtual {p0}, Ljava/lang/Boolean;->booleanValue()Z

    move-result p0

    if-eqz p0, :cond_15

    sget-object p0, Lcom/google/android/gms/ads/internal/client/zzba;->e:Lcom/google/android/gms/ads/internal/client/zzba;

    iget-object p0, p0, Lcom/google/android/gms/ads/internal/client/zzba;->c:Lcom/google/android/gms/internal/ads/zzbje;

    invoke-virtual {p0}, Lcom/google/android/gms/internal/ads/zzbje;->zzc()Z

    move-result p0

    if-eqz p0, :cond_15

    new-instance p0, Lcom/google/android/gms/internal/ads/zzcfq;

    const-string v0, ""

    invoke-direct {p0, v0, v1, v2}, Lcom/google/android/gms/internal/ads/zzcfq;-><init>(Ljava/lang/String;J)V

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->n:Lcom/google/android/gms/internal/ads/zzcfq;

    goto :goto_12

    :catchall_3
    move-exception v0

    move-object p0, v0

    goto/16 :goto_14

    :cond_15
    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "app_settings_json"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->n:Lcom/google/android/gms/internal/ads/zzcfq;

    invoke-virtual {v1}, Lcom/google/android/gms/internal/ads/zzcfq;->zzd()Ljava/lang/String;

    move-result-object v1

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iget-object v0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v1, "app_settings_last_update_ms"

    iget-object v2, v3, Lcom/google/android/gms/ads/internal/util/zzj;->n:Lcom/google/android/gms/internal/ads/zzcfq;

    invoke-virtual {v2}, Lcom/google/android/gms/internal/ads/zzcfq;->zzb()J

    move-result-wide v5

    invoke-interface {v0, v1, v5, v6}, Landroid/content/SharedPreferences;->getLong(Ljava/lang/String;J)J

    move-result-wide v0

    new-instance v2, Lcom/google/android/gms/internal/ads/zzcfq;

    invoke-direct {v2, p0, v0, v1}, Lcom/google/android/gms/internal/ads/zzcfq;-><init>(Ljava/lang/String;J)V

    iput-object v2, v3, Lcom/google/android/gms/ads/internal/util/zzj;->n:Lcom/google/android/gms/internal/ads/zzcfq;

    :goto_12
    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "app_last_background_time_ms"

    iget-wide v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->o:J

    invoke-interface {p0, v0, v1, v2}, Landroid/content/SharedPreferences;->getLong(Ljava/lang/String;J)J

    move-result-wide v0

    iput-wide v0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->o:J

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "request_in_session_count"

    iget v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->q:I

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->q:I

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "first_ad_req_time_ms"

    iget-wide v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->p:J

    invoke-interface {p0, v0, v1, v2}, Landroid/content/SharedPreferences;->getLong(Ljava/lang/String;J)J

    move-result-wide v0

    iput-wide v0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->p:J

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "never_pool_slots"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->s:Ljava/util/Set;

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getStringSet(Ljava/lang/String;Ljava/util/Set;)Ljava/util/Set;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->s:Ljava/util/Set;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "display_cutout"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->w:Ljava/lang/String;

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->w:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "app_measurement_npa"

    iget v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->B:I

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->B:I

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "sd_app_measure_npa"

    iget v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->C:I

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->C:I

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "sd_app_measure_npa_ts"

    iget-wide v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->D:J

    invoke-interface {p0, v0, v1, v2}, Landroid/content/SharedPreferences;->getLong(Ljava/lang/String;J)J

    move-result-wide v0

    iput-wide v0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->D:J

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "inspector_info"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->x:Ljava/lang/String;

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->x:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "linked_device"

    iget-boolean v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->y:Z

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    iput-boolean p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->y:Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "linked_ad_unit"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->z:Ljava/lang/String;

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->z:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "inspector_ui_storage"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->A:Ljava/lang/String;

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->A:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "IABTCF_TCString"

    iget-object v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->l:Ljava/lang/String;

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->l:Ljava/lang/String;

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "gad_has_consent_for_cookies"

    iget v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->m:I

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->m:I

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "is_install_referrer_reported"

    iget-boolean v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->E:Z

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    iput-boolean p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->E:Z

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "total_inflight_ad_limit"

    iget v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->F:I

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->F:I

    iget-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v0, "default_queue_capacity"

    iget v1, v3, Lcom/google/android/gms/ads/internal/util/zzj;->G:I

    invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p0

    iput p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->G:I
    :try_end_8
    .catchall {:try_start_8 .. :try_end_8} :catchall_3

    :try_start_9
    new-instance p0, Lyz0;

    iget-object v0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->f:Landroid/content/SharedPreferences;

    const-string v1, "native_advanced_settings"

    const-string v2, "{}"

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lyz0;-><init>(Ljava/lang/String;)V

    iput-object p0, v3, Lcom/google/android/gms/ads/internal/util/zzj;->t:Lyz0;
    :try_end_9
    .catch Lxz0; {:try_start_9 .. :try_end_9} :catch_4
    .catchall {:try_start_9 .. :try_end_9} :catchall_3

    goto :goto_13

    :catch_4
    move-exception v0

    move-object p0, v0

    :try_start_a
    const-string v0, "Could not convert native advanced settings to json object"

    sget v1, Lcom/google/android/gms/ads/internal/util/zze;->b:I

    invoke-static {v0, p0}, Lcom/google/android/gms/ads/internal/util/client/zzo;->g(Ljava/lang/String;Ljava/lang/Throwable;)V

    :goto_13
    invoke-virtual {v3}, Lcom/google/android/gms/ads/internal/util/zzj;->q()V

    monitor-exit v4

    goto :goto_15

    :goto_14
    monitor-exit v4
    :try_end_a
    .catchall {:try_start_a .. :try_end_a} :catchall_3

    :try_start_b
    throw p0
    :try_end_b
    .catchall {:try_start_b .. :try_end_b} :catchall_4

    :catchall_4
    move-exception v0

    move-object p0, v0

    const-string v0, "AdSharedPreferenceManagerImpl.initializeOnBackgroundThread"

    sget-object v1, Lcom/google/android/gms/ads/internal/zzt;->D:Lcom/google/android/gms/ads/internal/zzt;

    iget-object v1, v1, Lcom/google/android/gms/ads/internal/zzt;->h:Lcom/google/android/gms/internal/ads/zzcfv;

    invoke-virtual {v1, p0, v0}, Lcom/google/android/gms/internal/ads/zzcfv;->zzh(Ljava/lang/Throwable;Ljava/lang/String;)V

    const-string v0, "AdSharedPreferenceManagerImpl.initializeOnBackgroundThread, errorMessage = "

    invoke-static {v0, p0}, Lcom/google/android/gms/ads/internal/util/zze;->l(Ljava/lang/String;Ljava/lang/Throwable;)V

    :goto_15
    return-void

    :pswitch_e
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzhw;

    iget-object v1, v0, Lcom/google/android/gms/measurement/internal/zzhw;->b:Lcom/google/android/gms/measurement/internal/zzht;

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhw;->a:Ljava/lang/String;

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/internal/measurement/zzbz;

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzht;->a:Lcom/google/android/gms/measurement/internal/zzim;

    iget-object v2, v1, Lcom/google/android/gms/measurement/internal/zzim;->v:Lcom/google/android/gms/measurement/internal/zzij;

    iget-object v3, v1, Lcom/google/android/gms/measurement/internal/zzim;->u:Lcom/google/android/gms/measurement/internal/zzhc;

    invoke-static {v2}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    invoke-virtual {v2}, Lcom/google/android/gms/measurement/internal/zzij;->j()V

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    const-string v4, "package_name"

    invoke-virtual {v2, v4, v0}, Landroid/os/BaseBundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

    :try_start_c
    invoke-interface {p0, v2}, Lcom/google/android/gms/internal/measurement/zzbz;->zza(Landroid/os/Bundle;)Landroid/os/Bundle;

    move-result-object p0

    if-nez p0, :cond_16

    invoke-static {v3}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object p0, v3, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v0, "Install Referrer Service returned a null response"

    invoke-virtual {p0, v0}, Lcom/google/android/gms/measurement/internal/zzhe;->a(Ljava/lang/String;)V
    :try_end_c
    .catch Ljava/lang/Exception; {:try_start_c .. :try_end_c} :catch_5

    goto :goto_16

    :catch_5
    move-exception v0

    move-object p0, v0

    invoke-static {v3}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    iget-object v0, v3, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v2, "Exception occurred while retrieving the Install Referrer"

    invoke-virtual {p0}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, v2, p0}, Lcom/google/android/gms/measurement/internal/zzhe;->b(Ljava/lang/String;Ljava/lang/Object;)V

    :cond_16
    :goto_16
    iget-object p0, v1, Lcom/google/android/gms/measurement/internal/zzim;->v:Lcom/google/android/gms/measurement/internal/zzij;

    invoke-static {p0}, Lcom/google/android/gms/measurement/internal/zzim;->e(Ll73;)V

    invoke-virtual {p0}, Lcom/google/android/gms/measurement/internal/zzij;->j()V

    new-instance p0, Ljava/lang/IllegalStateException;

    const-string v0, "Unexpected call on client side"

    invoke-direct {p0, v0}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw p0

    :pswitch_f
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Ljava/util/concurrent/Callable;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/tasks/TaskCompletionSource;

    :try_start_d
    invoke-interface {v0}, Ljava/util/concurrent/Callable;->call()Ljava/lang/Object;

    move-result-object v0
    :try_end_d
    .catch Lnf1; {:try_start_d .. :try_end_d} :catch_7
    .catch Ljava/lang/Exception; {:try_start_d .. :try_end_d} :catch_6

    invoke-virtual {p0, v0}, Lcom/google/android/gms/tasks/TaskCompletionSource;->setResult(Ljava/lang/Object;)V

    goto :goto_17

    :catch_6
    move-exception v0

    new-instance v1, Lnf1;

    const-string v2, "Internal error has occurred when executing ML Kit tasks"

    invoke-direct {v1, v2, v0}, Lnf1;-><init>(Ljava/lang/String;Ljava/lang/Exception;)V

    invoke-virtual {p0, v1}, Lcom/google/android/gms/tasks/TaskCompletionSource;->setException(Ljava/lang/Exception;)V

    goto :goto_17

    :catch_7
    move-exception v0

    invoke-virtual {p0, v0}, Lcom/google/android/gms/tasks/TaskCompletionSource;->setException(Ljava/lang/Exception;)V

    :goto_17
    return-void

    :pswitch_10
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/ads/internal/client/zzek;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/dynamic/IObjectWrapper;

    invoke-static {p0}, Lcom/google/android/gms/dynamic/ObjectWrapper;->H1(Lcom/google/android/gms/dynamic/IObjectWrapper;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/View;

    iget-object v0, v0, Lcom/google/android/gms/ads/internal/client/zzek;->l:Lcom/google/android/gms/ads/BaseAdView;

    invoke-virtual {v0, p0}, Landroid/view/ViewGroup;->addView(Landroid/view/View;)V

    return-void

    :pswitch_11
    iget-object v0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/internal/ads/zzeaj;

    iget-object p0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast p0, Ljava/lang/Long;

    sget-object v1, Lcom/google/android/gms/ads/internal/zzt;->D:Lcom/google/android/gms/ads/internal/zzt;

    iget-object v1, v1, Lcom/google/android/gms/ads/internal/zzt;->k:Lcom/google/android/gms/common/util/DefaultClock;

    invoke-virtual {v1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v1

    invoke-virtual {p0}, Ljava/lang/Long;->longValue()J

    move-result-wide v3

    sub-long/2addr v1, v3

    const-string p0, "cld_r"

    invoke-static {v0, p0, v1, v2}, Lcom/google/android/gms/ads/internal/zzf;->b(Lcom/google/android/gms/internal/ads/zzeaj;Ljava/lang/String;J)V

    return-void

    :pswitch_12
    iget-object v0, p0, Lf53;->c:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/ads/AdRequest;

    iget-object p0, p0, Lf53;->b:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/ads/BaseAdView;

    :try_start_e
    iget-object v1, p0, Lcom/google/android/gms/ads/BaseAdView;->a:Lcom/google/android/gms/ads/internal/client/zzek;

    iget-object v0, v0, Lcom/google/android/gms/ads/AdRequest;->a:Lcom/google/android/gms/ads/internal/client/zzeh;

    invoke-virtual {v1, v0}, Lcom/google/android/gms/ads/internal/client/zzek;->b(Lcom/google/android/gms/ads/internal/client/zzeh;)V
    :try_end_e
    .catch Ljava/lang/IllegalStateException; {:try_start_e .. :try_end_e} :catch_8

    goto :goto_18

    :catch_8
    move-exception v0

    invoke-virtual {p0}, Landroid/view/View;->getContext()Landroid/content/Context;

    move-result-object p0

    invoke-static {p0}, Lcom/google/android/gms/internal/ads/zzcaq;->zza(Landroid/content/Context;)Lcom/google/android/gms/internal/ads/zzcas;

    move-result-object p0

    const-string v1, "BaseAdView.loadAd"

    invoke-interface {p0, v0, v1}, Lcom/google/android/gms/internal/ads/zzcas;->zzh(Ljava/lang/Throwable;Ljava/lang/String;)V

    :goto_18
    return-void

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method
