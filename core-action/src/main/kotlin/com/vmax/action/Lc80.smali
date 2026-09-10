.class public final Lc80;
.super Ljava/lang/Object;
.source "SourceFile"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field public final synthetic a:I

.field public final synthetic b:Ljava/lang/Object;

.field public final synthetic c:Ljava/lang/Object;

.field public final synthetic d:Ljava/lang/Object;

.field public final synthetic e:Ljava/lang/Object;

.field public final synthetic f:Ljava/lang/Object;


# direct methods
.method public constructor <init>(Lcom/google/android/gms/measurement/internal/zzmp;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzq;Lcom/google/android/gms/internal/measurement/zzdq;)V
    .locals 1

    const/4 v0, 0x5

    iput v0, p0, Lc80;->a:I

    .line 19
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p2, p0, Lc80;->c:Ljava/lang/Object;

    iput-object p3, p0, Lc80;->b:Ljava/lang/Object;

    iput-object p4, p0, Lc80;->d:Ljava/lang/Object;

    iput-object p5, p0, Lc80;->e:Ljava/lang/Object;

    iput-object p1, p0, Lc80;->f:Ljava/lang/Object;

    return-void
.end method

.method public synthetic constructor <init>(Lhl2;Lcom/google/android/gms/tasks/CancellationToken;Lcom/google/android/gms/tasks/CancellationTokenSource;Ljava/util/concurrent/Callable;Lcom/google/android/gms/tasks/TaskCompletionSource;)V
    .locals 1

    const/4 v0, 0x3

    iput v0, p0, Lc80;->a:I

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lc80;->b:Ljava/lang/Object;

    iput-object p2, p0, Lc80;->c:Ljava/lang/Object;

    iput-object p3, p0, Lc80;->d:Ljava/lang/Object;

    iput-object p4, p0, Lc80;->e:Ljava/lang/Object;

    iput-object p5, p0, Lc80;->f:Ljava/lang/Object;

    return-void
.end method

.method public synthetic constructor <init>(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;I)V
    .locals 0

    .line 17
    iput p6, p0, Lc80;->a:I

    iput-object p1, p0, Lc80;->f:Ljava/lang/Object;

    iput-object p2, p0, Lc80;->b:Ljava/lang/Object;

    iput-object p3, p0, Lc80;->c:Ljava/lang/Object;

    iput-object p4, p0, Lc80;->d:Ljava/lang/Object;

    iput-object p5, p0, Lc80;->e:Ljava/lang/Object;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public synthetic constructor <init>(Lx23;Landroid/app/Activity;Ljava/lang/String;Ljava/lang/String;Ljava/util/HashMap;)V
    .locals 1

    const/4 v0, 0x2

    iput v0, p0, Lc80;->a:I

    .line 18
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lc80;->b:Ljava/lang/Object;

    iput-object p2, p0, Lc80;->d:Ljava/lang/Object;

    iput-object p3, p0, Lc80;->c:Ljava/lang/Object;

    iput-object p4, p0, Lc80;->e:Ljava/lang/Object;

    iput-object p5, p0, Lc80;->f:Ljava/lang/Object;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 7

    iget v0, p0, Lc80;->a:I

    const/4 v1, 0x0

    const/4 v2, 0x0

    packed-switch v0, :pswitch_data_0

    iget-object v0, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v0, Lcom/google/android/gms/measurement/internal/zzq;

    iget-object v1, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v1, Ljava/lang/String;

    iget-object v2, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v2, Ljava/lang/String;

    iget-object v3, p0, Lc80;->e:Ljava/lang/Object;

    check-cast v3, Lcom/google/android/gms/internal/measurement/zzdq;

    iget-object p0, p0, Lc80;->f:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/measurement/internal/zzmp;

    new-instance v4, Ljava/util/ArrayList;

    invoke-direct {v4}, Ljava/util/ArrayList;-><init>()V

    :try_start_0
    iget-object v5, p0, Lcom/google/android/gms/measurement/internal/zzmp;->e:Lcom/google/android/gms/measurement/internal/zzgk;

    if-nez v5, :cond_0

    invoke-virtual {p0}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v0

    iget-object v0, v0, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v5, "Failed to get conditional properties; not connected to service"

    invoke-virtual {v0, v5, v2, v1}, Lcom/google/android/gms/measurement/internal/zzhe;->c(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
    :try_end_0
    .catch Landroid/os/RemoteException; {:try_start_0 .. :try_end_0} :catch_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    invoke-virtual {p0}, Lmv;->i()Lcom/google/android/gms/measurement/internal/zzqd;

    move-result-object p0

    invoke-virtual {p0, v3, v4}, Lcom/google/android/gms/measurement/internal/zzqd;->H(Lcom/google/android/gms/internal/measurement/zzdq;Ljava/util/ArrayList;)V

    goto :goto_1

    :catchall_0
    move-exception v0

    goto :goto_2

    :catch_0
    move-exception v0

    goto :goto_0

    :cond_0
    :try_start_1
    invoke-interface {v5, v2, v1, v0}, Lcom/google/android/gms/measurement/internal/zzgk;->s1(Ljava/lang/String;Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzq;)Ljava/util/List;

    move-result-object v0

    invoke-static {v0}, Lcom/google/android/gms/measurement/internal/zzqd;->e0(Ljava/util/List;)Ljava/util/ArrayList;

    move-result-object v4

    invoke-virtual {p0}, Lcom/google/android/gms/measurement/internal/zzmp;->C()V
    :try_end_1
    .catch Landroid/os/RemoteException; {:try_start_1 .. :try_end_1} :catch_0
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    invoke-virtual {p0}, Lmv;->i()Lcom/google/android/gms/measurement/internal/zzqd;

    move-result-object p0

    invoke-virtual {p0, v3, v4}, Lcom/google/android/gms/measurement/internal/zzqd;->H(Lcom/google/android/gms/internal/measurement/zzdq;Ljava/util/ArrayList;)V

    goto :goto_1

    :goto_0
    :try_start_2
    invoke-virtual {p0}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v5

    iget-object v5, v5, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v6, "Failed to get conditional properties; remote exception"

    invoke-virtual {v5, v6, v2, v1, v0}, Lcom/google/android/gms/measurement/internal/zzhe;->d(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    invoke-virtual {p0}, Lmv;->i()Lcom/google/android/gms/measurement/internal/zzqd;

    move-result-object p0

    invoke-virtual {p0, v3, v4}, Lcom/google/android/gms/measurement/internal/zzqd;->H(Lcom/google/android/gms/internal/measurement/zzdq;Ljava/util/ArrayList;)V

    :goto_1
    return-void

    :goto_2
    invoke-virtual {p0}, Lmv;->i()Lcom/google/android/gms/measurement/internal/zzqd;

    move-result-object p0

    invoke-virtual {p0, v3, v4}, Lcom/google/android/gms/measurement/internal/zzqd;->H(Lcom/google/android/gms/internal/measurement/zzdq;Ljava/util/ArrayList;)V

    throw v0

    :pswitch_0
    iget-object v0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v0, Ljava/util/concurrent/atomic/AtomicReference;

    monitor-enter v0

    :try_start_3
    iget-object v1, p0, Lc80;->f:Ljava/lang/Object;

    check-cast v1, Lcom/google/android/gms/measurement/internal/zzmp;

    iget-object v3, v1, Lcom/google/android/gms/measurement/internal/zzmp;->e:Lcom/google/android/gms/measurement/internal/zzgk;

    if-nez v3, :cond_1

    invoke-virtual {v1}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v1

    iget-object v1, v1, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v3, "(legacy) Failed to get conditional properties; not connected to service"

    iget-object v4, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v4, Ljava/lang/String;

    iget-object v5, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v5, Ljava/lang/String;

    invoke-virtual {v1, v3, v2, v4, v5}, Lcom/google/android/gms/measurement/internal/zzhe;->d(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V

    iget-object v1, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v1, Ljava/util/concurrent/atomic/AtomicReference;

    sget-object v3, Ljava/util/Collections;->EMPTY_LIST:Ljava/util/List;

    invoke-virtual {v1, v3}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V
    :try_end_3
    .catch Landroid/os/RemoteException; {:try_start_3 .. :try_end_3} :catch_1
    .catchall {:try_start_3 .. :try_end_3} :catchall_2

    :try_start_4
    iget-object p0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast p0, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {p0}, Ljava/lang/Object;->notify()V

    monitor-exit v0
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    goto :goto_6

    :catchall_1
    move-exception p0

    goto :goto_8

    :catchall_2
    move-exception v1

    goto :goto_7

    :catch_1
    move-exception v1

    goto :goto_4

    :cond_1
    :try_start_5
    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_2

    iget-object v1, p0, Lc80;->e:Ljava/lang/Object;

    check-cast v1, Lcom/google/android/gms/measurement/internal/zzq;

    iget-object v4, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v4, Ljava/util/concurrent/atomic/AtomicReference;

    iget-object v5, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v5, Ljava/lang/String;

    iget-object v6, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v6, Ljava/lang/String;

    invoke-interface {v3, v5, v6, v1}, Lcom/google/android/gms/measurement/internal/zzgk;->s1(Ljava/lang/String;Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzq;)Ljava/util/List;

    move-result-object v1

    invoke-virtual {v4, v1}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V

    goto :goto_3

    :cond_2
    iget-object v1, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v1, Ljava/util/concurrent/atomic/AtomicReference;

    iget-object v4, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v4, Ljava/lang/String;

    iget-object v5, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v5, Ljava/lang/String;

    invoke-interface {v3, v2, v4, v5}, Lcom/google/android/gms/measurement/internal/zzgk;->K(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V

    :goto_3
    iget-object v1, p0, Lc80;->f:Ljava/lang/Object;

    check-cast v1, Lcom/google/android/gms/measurement/internal/zzmp;

    invoke-virtual {v1}, Lcom/google/android/gms/measurement/internal/zzmp;->C()V
    :try_end_5
    .catch Landroid/os/RemoteException; {:try_start_5 .. :try_end_5} :catch_1
    .catchall {:try_start_5 .. :try_end_5} :catchall_2

    :try_start_6
    iget-object p0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast p0, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {p0}, Ljava/lang/Object;->notify()V
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_1

    goto :goto_5

    :goto_4
    :try_start_7
    iget-object v3, p0, Lc80;->f:Ljava/lang/Object;

    check-cast v3, Lcom/google/android/gms/measurement/internal/zzmp;

    invoke-virtual {v3}, Lmv;->zzj()Lcom/google/android/gms/measurement/internal/zzhc;

    move-result-object v3

    iget-object v3, v3, Lcom/google/android/gms/measurement/internal/zzhc;->s:Lcom/google/android/gms/measurement/internal/zzhe;

    const-string v4, "(legacy) Failed to get conditional properties; remote exception"

    iget-object v5, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v5, Ljava/lang/String;

    invoke-virtual {v3, v4, v2, v5, v1}, Lcom/google/android/gms/measurement/internal/zzhe;->d(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V

    iget-object v1, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v1, Ljava/util/concurrent/atomic/AtomicReference;

    sget-object v2, Ljava/util/Collections;->EMPTY_LIST:Ljava/util/List;

    invoke-virtual {v1, v2}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V
    :try_end_7
    .catchall {:try_start_7 .. :try_end_7} :catchall_2

    :try_start_8
    iget-object p0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast p0, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {p0}, Ljava/lang/Object;->notify()V

    :goto_5
    monitor-exit v0

    :goto_6
    return-void

    :goto_7
    iget-object p0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast p0, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {p0}, Ljava/lang/Object;->notify()V

    throw v1

    :goto_8
    monitor-exit v0
    :try_end_8
    .catchall {:try_start_8 .. :try_end_8} :catchall_1

    throw p0

    :pswitch_1
    iget-object v0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v0, Lhl2;

    iget-object v1, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v1, Lcom/google/android/gms/tasks/CancellationToken;

    iget-object v2, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v2, Lcom/google/android/gms/tasks/CancellationTokenSource;

    iget-object v3, p0, Lc80;->e:Ljava/lang/Object;

    check-cast v3, Ljava/util/concurrent/Callable;

    iget-object p0, p0, Lc80;->f:Ljava/lang/Object;

    check-cast p0, Lcom/google/android/gms/tasks/TaskCompletionSource;

    invoke-virtual {v1}, Lcom/google/android/gms/tasks/CancellationToken;->isCancellationRequested()Z

    move-result v4

    if-eqz v4, :cond_3

    invoke-virtual {v2}, Lcom/google/android/gms/tasks/CancellationTokenSource;->cancel()V

    goto :goto_c

    :cond_3
    :try_start_9
    iget-object v4, v0, Lhl2;->c:Ljava/util/concurrent/atomic/AtomicBoolean;

    invoke-virtual {v4}, Ljava/util/concurrent/atomic/AtomicBoolean;->get()Z

    move-result v4

    if-nez v4, :cond_4

    monitor-enter v0
    :try_end_9
    .catch Ljava/lang/RuntimeException; {:try_start_9 .. :try_end_9} :catch_3
    .catch Ljava/lang/Exception; {:try_start_9 .. :try_end_9} :catch_2

    :try_start_a
    iget-object v4, v0, Lhl2;->d:Lg83;

    invoke-interface {v4}, Lg83;->zzb()V
    :try_end_a
    .catchall {:try_start_a .. :try_end_a} :catchall_3

    :try_start_b
    monitor-exit v0

    iget-object v0, v0, Lhl2;->c:Ljava/util/concurrent/atomic/AtomicBoolean;

    const/4 v4, 0x1

    invoke-virtual {v0, v4}, Ljava/util/concurrent/atomic/AtomicBoolean;->set(Z)V
    :try_end_b
    .catch Ljava/lang/RuntimeException; {:try_start_b .. :try_end_b} :catch_3
    .catch Ljava/lang/Exception; {:try_start_b .. :try_end_b} :catch_2

    goto :goto_9

    :catch_2
    move-exception v0

    goto :goto_b

    :catch_3
    move-exception v0

    goto :goto_a

    :catchall_3
    move-exception v3

    :try_start_c
    monitor-exit v0
    :try_end_c
    .catchall {:try_start_c .. :try_end_c} :catchall_3

    :try_start_d
    throw v3

    :cond_4
    :goto_9
    invoke-virtual {v1}, Lcom/google/android/gms/tasks/CancellationToken;->isCancellationRequested()Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-virtual {v2}, Lcom/google/android/gms/tasks/CancellationTokenSource;->cancel()V

    goto :goto_c

    :cond_5
    invoke-interface {v3}, Ljava/util/concurrent/Callable;->call()Ljava/lang/Object;

    move-result-object v0
    :try_end_d
    .catch Ljava/lang/RuntimeException; {:try_start_d .. :try_end_d} :catch_3
    .catch Ljava/lang/Exception; {:try_start_d .. :try_end_d} :catch_2

    :try_start_e
    invoke-virtual {v1}, Lcom/google/android/gms/tasks/CancellationToken;->isCancellationRequested()Z

    move-result v3

    if-eqz v3, :cond_6

    invoke-virtual {v2}, Lcom/google/android/gms/tasks/CancellationTokenSource;->cancel()V

    goto :goto_c

    :cond_6
    invoke-virtual {p0, v0}, Lcom/google/android/gms/tasks/TaskCompletionSource;->setResult(Ljava/lang/Object;)V

    goto :goto_c

    :goto_a
    new-instance v3, Lnf1;

    const-string v4, "Internal error has occurred when executing ML Kit tasks"

    invoke-direct {v3, v4, v0}, Lnf1;-><init>(Ljava/lang/String;Ljava/lang/Exception;)V

    throw v3
    :try_end_e
    .catch Ljava/lang/Exception; {:try_start_e .. :try_end_e} :catch_2

    :goto_b
    invoke-virtual {v1}, Lcom/google/android/gms/tasks/CancellationToken;->isCancellationRequested()Z

    move-result v1

    if-eqz v1, :cond_7

    invoke-virtual {v2}, Lcom/google/android/gms/tasks/CancellationTokenSource;->cancel()V

    goto :goto_c

    :cond_7
    invoke-virtual {p0, v0}, Lcom/google/android/gms/tasks/TaskCompletionSource;->setException(Ljava/lang/Exception;)V

    :goto_c
    return-void

    :pswitch_2
    iget-object v0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v0, Lx23;

    iget-object v0, v0, Lx23;->s:La33;

    invoke-virtual {v0}, La33;->c()V

    iget-object v0, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v0, Ljava/lang/String;

    iget-object v2, p0, Lc80;->e:Ljava/lang/Object;

    check-cast v2, Ljava/lang/String;

    iget-object v3, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v3, Landroid/app/Activity;

    iget-object p0, p0, Lc80;->f:Ljava/lang/Object;

    check-cast p0, Ljava/util/HashMap;

    invoke-static {v0, v2, p0}, Lbf0;->y(Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Landroid/content/Intent;

    move-result-object p0

    invoke-virtual {v3, p0, v1}, Landroid/app/Activity;->startActivityForResult(Landroid/content/Intent;I)V

    return-void

    :pswitch_3
    iget-object v0, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v0, Ljava/util/Collection;

    invoke-interface {v0}, Ljava/util/Collection;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_8
    :goto_d
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_9

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lm22;

    iget-object v4, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v4, Lm22;

    if-eq v3, v4, :cond_8

    iget-object v3, v3, Lm22;->a:Lan;

    sget-object v4, Lha1;->G:Ldf2;

    invoke-interface {v3, v4}, Lan;->c(Ldf2;)V

    goto :goto_d

    :cond_9
    iget-object v0, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v0, Ljava/util/concurrent/Future;

    if-eqz v0, :cond_a

    invoke-interface {v0, v1}, Ljava/util/concurrent/Future;->cancel(Z)Z

    :cond_a
    iget-object v0, p0, Lc80;->e:Ljava/lang/Object;

    check-cast v0, Ljava/util/concurrent/Future;

    if-eqz v0, :cond_b

    invoke-interface {v0, v1}, Ljava/util/concurrent/Future;->cancel(Z)Z

    :cond_b
    iget-object p0, p0, Lc80;->f:Ljava/lang/Object;

    check-cast p0, Lha1;

    iget-object v0, p0, Lha1;->D:Lgs;

    iget-object v0, v0, Lgs;->b:Ljava/lang/Object;

    check-cast v0, Lua1;

    iget-object v0, v0, Lua1;->H:Lz82;

    iget-object v1, v0, Lz82;->b:Ljava/lang/Object;

    monitor-enter v1

    :try_start_f
    iget-object v3, v0, Lz82;->c:Ljava/lang/Object;

    check-cast v3, Ljava/util/HashSet;

    invoke-virtual {v3, p0}, Ljava/util/HashSet;->remove(Ljava/lang/Object;)Z

    iget-object p0, v0, Lz82;->c:Ljava/lang/Object;

    check-cast p0, Ljava/util/HashSet;

    invoke-virtual {p0}, Ljava/util/HashSet;->isEmpty()Z

    move-result p0

    if-eqz p0, :cond_c

    iget-object p0, v0, Lz82;->d:Ljava/lang/Object;

    move-object v2, p0

    check-cast v2, Ldf2;

    new-instance p0, Ljava/util/HashSet;

    invoke-direct {p0}, Ljava/util/HashSet;-><init>()V

    iput-object p0, v0, Lz82;->c:Ljava/lang/Object;

    goto :goto_e

    :catchall_4
    move-exception p0

    goto :goto_f

    :cond_c
    :goto_e
    monitor-exit v1
    :try_end_f
    .catchall {:try_start_f .. :try_end_f} :catchall_4

    if-eqz v2, :cond_d

    iget-object p0, v0, Lz82;->e:Ljava/lang/Object;

    check-cast p0, Lua1;

    iget-object p0, p0, Lua1;->G:Lm10;

    invoke-virtual {p0, v2}, Lm10;->e(Ldf2;)V

    :cond_d
    return-void

    :goto_f
    :try_start_10
    monitor-exit v1
    :try_end_10
    .catchall {:try_start_10 .. :try_end_10} :catchall_4

    throw p0

    :pswitch_4
    invoke-static {}, Lorg/mozilla/gecko/GeckoJavaSampler;->h()Ljava/lang/Double;

    move-result-object v0

    iget-object v1, p0, Lc80;->b:Ljava/lang/Object;

    check-cast v1, Lah;

    iget-object v3, p0, Lc80;->c:Ljava/lang/Object;

    check-cast v3, Ljava/lang/String;

    iget-object v4, p0, Lc80;->d:Ljava/lang/Object;

    check-cast v4, Lorg/mozilla/gecko/util/GeckoBundle;

    iget-object p0, p0, Lc80;->e:Ljava/lang/Object;

    check-cast p0, Lorg/mozilla/gecko/util/EventCallback;

    invoke-interface {v1, v3, v4, p0}, Lah;->handleMessage(Ljava/lang/String;Lorg/mozilla/gecko/util/GeckoBundle;Lorg/mozilla/gecko/util/EventCallback;)V

    const-string p0, "EventDispatcher handleMessage"

    invoke-static {p0, v0, v2, v3}, Lorg/mozilla/gecko/GeckoJavaSampler;->b(Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/String;)V

    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method
