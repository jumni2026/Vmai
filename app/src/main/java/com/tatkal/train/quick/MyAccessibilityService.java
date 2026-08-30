.class public Lcom/tatkal/train/quick/MyAccessibilityService;
.super Landroid/accessibilityservice/AccessibilityService;
.source "SourceFile"


# static fields
.field public static Z1:Lcom/tatkal/train/quick/MyAccessibilityService; = null

.field public static a2:Ljava/lang/String; = null

.field public static b2:I = 0x0

.field public static c2:I = 0x0

.field public static d2:I = 0x0

.field public static e2:Ljava/lang/String; = ""

.field public static f2:I

.field public static g2:Landroid/view/accessibility/AccessibilityNodeInfo;

.field public static h2:Landroid/view/accessibility/AccessibilityNodeInfo;


# instance fields
.field public A:I

.field public A0:Ljava/lang/String;

.field public A1:Z

.field public B:I

.field public B0:Ljava/lang/String;

.field public B1:Z

.field public C:Z

.field public C0:Z

.field public C1:Z

.field public D:Z

.field public D0:Z

.field public D1:Z

.field public E:Z

.field public E0:Z

.field public E1:Z

.field public F:Z

.field public F0:I

.field public F1:Z

.field public G:I

.field public G0:Z

.field public G1:Z

.field public H:Z

.field public H0:Z

.field public H1:I

.field public I:I

.field public I0:Z

.field public I1:I

.field public J:I

.field public J0:Ljava/util/Timer;

.field public J1:Ljava/lang/String;

.field public K:Z

.field public K0:Loh1;

.field public K1:I

.field public L:I

.field public final L0:Landroid/os/Handler;

.field public L1:Lkf1;

.field public M:Z

.field public M0:I

.field public M1:Z

.field public N:Z

.field public N0:Ljava/util/Timer;

.field public N1:I

.field public final O:[Ljava/lang/String;

.field public O0:Landroid/view/accessibility/AccessibilityNodeInfo;

.field public O1:Z

.field public P:Ljava/lang/String;

.field public P0:Landroid/view/accessibility/AccessibilityNodeInfo;

.field public P1:J

.field public Q:Ljava/util/HashMap;

.field public Q0:I

.field public Q1:Z

.field public final R:[Ljava/lang/String;

.field public R0:I

.field public R1:Z

.field public final S:[Ljava/lang/String;

.field public S0:Z

.field public S1:Z

.field public final T:Ljava/lang/String;

.field public T0:Z

.field public T1:Z

.field public U:Ljava/lang/String;

.field public U0:I

.field public U1:Ljava/lang/String;

.field public V:Ljava/lang/String;

.field public V0:I

.field public V1:I

.field public W:Ljava/lang/String;

.field public W0:I

.field public W1:I

.field public X:Ljava/lang/String;

.field public X0:Landroid/view/accessibility/AccessibilityNodeInfo;

.field public final X1:Lph1;

.field public Y:Ljava/lang/String;

.field public Y0:Landroid/view/accessibility/AccessibilityNodeInfo;

.field public final Y1:Ljava/util/concurrent/ExecutorService;

.field public Z:Ljava/lang/String;

.field public Z0:Landroid/view/accessibility/AccessibilityNodeInfo;

.field public a:Lcom/tatkal/train/quick/FloatingWidgetService;

.field public a0:I

.field public a1:Ljava/lang/String;

.field public b:Z

.field public b0:Ljava/lang/String;

.field public b1:I

.field public c:Z

.field public c0:Ljava/lang/String;

.field public c1:Z

.field public d:Z

.field public d0:Ljava/lang/String;

.field public final d1:[Z

.field public e:Z

.field public e0:[Ldo1;

.field public e1:Ljava/lang/String;

.field public f:Z

.field public f0:[Lrl;

.field public f1:Z

.field public g0:Ljava/lang/String;

.field public g1:Z

.field public h0:I

.field public h1:Ljava/lang/String;

.field public i0:Z

.field public i1:I

.field public j0:Z

.field public j1:I

.field public k0:Z

.field public k1:I

.field public l0:Z

.field public l1:I

.field public m0:I

.field public m1:Z

.field public n0:I

.field public n1:Z

.field public o0:Z

.field public o1:Z

.field public p0:Ljava/lang/String;

.field public p1:Lcom/tatkal/train/quick/AdvancedWebView;

.field public q0:I

.field public q1:I

.field public r0:I

.field public r1:I

.field public s:Z

.field public final s0:Ljava/util/HashMap;

.field public s1:I

.field public t:Z

.field public t0:Ljava/lang/String;

.field public t1:Z

.field public u:Z

.field public u0:Ljava/lang/String;

.field public u1:Z

.field public v:Z

.field public v0:Z

.field public v1:I

.field public w:I

.field public w0:Z

.field public w1:Z

.field public x:I

.field public x0:I

.field public x1:Lcom/tatkal/train/quick/OTPBroadcastReceiver;

.field public y:I

.field public y0:Z

.field public y1:Z

.field public z:I

.field public z0:Z

.field public z1:Z


# direct methods
.method public constructor <init>()V
    .locals 13

    invoke-direct {p0}, Landroid/accessibilityservice/AccessibilityService;-><init>()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->b:Z

    const/16 v0, 0x63

    iput v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    const-string v11, "November"

    const-string v12, "December"

    const-string v1, "January"

    const-string v2, "February"

    const-string v3, "March"

    const-string v4, "April"

    const-string v5, "May"

    const-string v6, "June"

    const-string v7, "July"

    const-string v8, "August"

    const-string v9, "September"

    const-string v10, "October"

    filled-new-array/range {v1 .. v12}, [Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->O:[Ljava/lang/String;

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "click_jio_money"

    const-string v6, "click_airtel"

    const-string v1, "click_ewallet"

    const-string v2, "click_mobikwik"

    const-string v3, "click_paytm_wallet"

    const-string v4, "click_ola_money"

    filled-new-array/range {v1 .. v6}, [Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->R:[Ljava/lang/String;

    const-string v8, "click_hdfc_mpp"

    const-string v9, "click_airpay"

    const-string v1, "click_ipay"

    const-string v2, "click_paytm"

    const-string v3, "click_mobimpp"

    const-string v4, "click_payu_mpp"

    const-string v5, "click_razor_pay"

    const-string v6, "click_phone_pe"

    const-string v7, "click_icici_mpp"

    filled-new-array/range {v1 .. v9}, [Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S:[Ljava/lang/String;

    const-string v0, "click_paytm_upi"

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->T:Ljava/lang/String;

    const-string v0, "ll_from_station_layout"

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->U:Ljava/lang/String;

    const-string v0, "ll_to_station_layout"

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->V:Ljava/lang/String;

    const-string v0, ""

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p0:Ljava/lang/String;

    new-instance v1, Ljava/util/HashMap;

    invoke-direct {v1}, Ljava/util/HashMap;-><init>()V

    iput-object v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->s0:Ljava/util/HashMap;

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->A0:Ljava/lang/String;

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->B0:Ljava/lang/String;

    new-instance v1, Landroid/os/Handler;

    invoke-direct {v1}, Landroid/os/Handler;-><init>()V

    iput-object v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->L0:Landroid/os/Handler;

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    const/16 v1, 0xf

    new-array v1, v1, [Z

    iput-object v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    const/4 v1, -0x1

    iput v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->q1:I

    const-string v1, "NA"

    iput-object v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->J1:Ljava/lang/String;

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->U1:Ljava/lang/String;

    new-instance v0, Lph1;

    const/4 v1, 0x0

    invoke-direct {v0, p0, v1}, Lph1;-><init>(Ljava/lang/Object;I)V

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->X1:Lph1;

    invoke-static {}, Ljava/util/concurrent/Executors;->newSingleThreadExecutor()Ljava/util/concurrent/ExecutorService;

    move-result-object v0

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Y1:Ljava/util/concurrent/ExecutorService;

    new-instance p0, Landroid/os/Handler;

    invoke-static {}, Landroid/os/Looper;->getMainLooper()Landroid/os/Looper;

    move-result-object v0

    invoke-direct {p0, v0}, Landroid/os/Handler;-><init>(Landroid/os/Looper;)V

    return-void
.end method

.method public static a(Lcom/tatkal/train/quick/MyAccessibilityService;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 6

    const-string v0, "Availability Search"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "cris.org.in.prs.ima:id/lv_train_list"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_7

    const/4 v2, 0x0

    invoke-interface {v1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const-string v3, ""

    sput-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {p1, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    const-string v4, "cris.org.in.prs.ima:id/tv_close"

    invoke-virtual {p1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_0

    invoke-interface {v4, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    :cond_0
    iget v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    const-string v5, "cris.org.in.prs.ima:id/rv_train_class"

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v4, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    iget v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W0:I

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    iput-object v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z0:Landroid/view/accessibility/AccessibilityNodeInfo;

    iget v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    const-string v5, "cris.org.in.prs.ima:id/tv_avl_detail"

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_1

    invoke-interface {v4, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v5

    iput-object v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->X0:Landroid/view/accessibility/AccessibilityNodeInfo;

    :cond_1
    const-string v5, "cris.org.in.prs.ima:id/tv_continue"

    invoke-virtual {p1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    iput-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Y0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result p1

    if-lez p1, :cond_2

    invoke-interface {v4, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object p1

    invoke-interface {p1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    :cond_2
    iget p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    invoke-virtual {v1, p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    const-string v1, "cris.org.in.prs.ima:id/tv_otherdate"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_3

    invoke-interface {p1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    :cond_3
    iget p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->U0:I

    if-lez p1, :cond_7

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result p1

    if-nez p1, :cond_7

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    const-string v0, "#"

    invoke-virtual {p1, v0}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result p1

    const/4 v0, 0x1

    const/16 v1, 0x10

    if-eqz p1, :cond_4

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->U0:I

    return-void

    :cond_4
    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    invoke-virtual {p1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_7

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->J0:Ljava/util/Timer;

    if-eqz p1, :cond_5

    invoke-virtual {p1}, Ljava/util/Timer;->cancel()V

    const/4 p1, 0x0

    iput-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->J0:Ljava/util/Timer;

    :cond_5
    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iput-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    iput-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    iput-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    iput-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    const/4 p1, 0x2

    iput p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    iget-boolean p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    if-nez p1, :cond_6

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Y0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    :cond_6
    iput-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->U0:I

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    iget-object p0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    invoke-virtual {p1, p0}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method public static d(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/util/ArrayList;)V
    .locals 3

    if-nez p0, :cond_0

    goto :goto_1

    :cond_0
    const-string v0, "android.widget.ImageView"

    invoke-virtual {p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/String;->contentEquals(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p1, p0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    :cond_1
    invoke-virtual {p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    const/4 v1, 0x0

    :goto_0
    if-ge v1, v0, :cond_2

    invoke-virtual {p0, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-static {v2, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->d(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/util/ArrayList;)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    :goto_1
    return-void
.end method

.method public static h(Landroid/view/accessibility/AccessibilityNodeInfo;)Landroid/view/accessibility/AccessibilityNodeInfo;
    .locals 8

    invoke-virtual {p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v1

    const-string v2, "bottomPayButton"

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getViewIdResourceName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    return-object p0

    :cond_0
    const/4 v1, 0x0

    move v3, v1

    :goto_0
    if-ge v3, v0, :cond_5

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v5

    if-nez v5, :cond_1

    goto :goto_2

    :cond_1
    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getViewIdResourceName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v6

    invoke-virtual {v5, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    return-object v4

    :cond_2
    if-lez v6, :cond_4

    move v5, v1

    :goto_1
    if-ge v5, v6, :cond_4

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v7

    invoke-static {v7}, Lcom/tatkal/train/quick/MyAccessibilityService;->h(Landroid/view/accessibility/AccessibilityNodeInfo;)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v7

    if-eqz v7, :cond_3

    return-object v7

    :cond_3
    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_4
    :goto_2
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_5
    const/4 p0, 0x0

    return-object p0
.end method

.method public static w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V
    .locals 19

    move-object/from16 v0, p0

    move/from16 v1, p1

    if-nez v0, :cond_0

    const-string v0, "NULL"

    sput-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    return-void

    :cond_0
    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v2

    const/4 v4, 0x0

    :goto_0
    const-string v5, "\t"

    if-ge v4, v1, :cond_1

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v7, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v6, v7, v5}, Lt30;->i(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    sput-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    :cond_1
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v6, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v6, "[@"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getViewIdResourceName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "] | "

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    sput-object v4, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v4

    const-string v8, "\n"

    const-string v9, "]\n"

    const-string v10, " ["

    const-string v11, ""

    if-eqz v4, :cond_2

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v4

    invoke-interface {v4}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v4

    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v13, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v12, v13, v10, v4, v9}, Lq90;->o(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v12

    sput-object v12, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    goto :goto_1

    :cond_2
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v12, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v4, v12, v8}, Lt30;->i(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    sput-object v4, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    move-object v4, v11

    :goto_1
    sget-object v12, Lcom/tatkal/train/quick/MyAccessibilityService;->g2:Landroid/view/accessibility/AccessibilityNodeInfo;

    const-string v13, "Pay Securely"

    const-string v14, "@net.one97.paytm:id/coordinator"

    if-eqz v12, :cond_3

    invoke-virtual {v12, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v12

    invoke-interface {v12}, Ljava/util/List;->isEmpty()Z

    move-result v12

    if-nez v12, :cond_3

    invoke-virtual {v4, v13}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v12

    if-eqz v12, :cond_3

    sput-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->h2:Landroid/view/accessibility/AccessibilityNodeInfo;

    :cond_3
    const/4 v12, 0x0

    :goto_2
    if-ge v12, v2, :cond_8

    invoke-virtual {v0, v12}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v15

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getViewIdResourceName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v1

    move/from16 v16, v2

    const/4 v2, 0x0

    :goto_3
    move-object/from16 v17, v4

    add-int/lit8 v4, p1, 0x1

    if-ge v2, v4, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    move/from16 v18, v2

    sget-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v4, v2, v5}, Lt30;->i(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    sput-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    add-int/lit8 v2, v18, 0x1

    move-object/from16 v4, v17

    goto :goto_3

    :cond_4
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v2, v4, v0, v6, v3}, Lq90;->t(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    invoke-static {v2, v1, v7}, Lx42;->n(Ljava/lang/StringBuilder;ILjava/lang/String;)Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    if-eqz v0, :cond_5

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-interface {v0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v2, v3, v10, v0, v9}, Lq90;->o(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    sput-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    move-object v4, v0

    goto :goto_4

    :cond_5
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {v0, v2, v8}, Lt30;->i(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    move-object/from16 v4, v17

    :goto_4
    sget-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->g2:Landroid/view/accessibility/AccessibilityNodeInfo;

    if-eqz v0, :cond_6

    invoke-virtual {v0, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_6

    invoke-virtual {v4, v13}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_6

    sput-object v15, Lcom/tatkal/train/quick/MyAccessibilityService;->h2:Landroid/view/accessibility/AccessibilityNodeInfo;

    :cond_6
    if-lez v1, :cond_7

    const/4 v0, 0x0

    :goto_5
    if-ge v0, v1, :cond_7

    invoke-virtual {v15, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    add-int/lit8 v3, p1, 0x2

    invoke-static {v2, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_5

    :cond_7
    add-int/lit8 v12, v12, 0x1

    move-object/from16 v0, p0

    move/from16 v1, p1

    move/from16 v2, v16

    goto/16 :goto_2

    :cond_8
    return-void
.end method


# virtual methods
.method public final b(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 32

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    const-string v3, "NA"

    const-string v4, "CNF"

    iget v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N1:I

    const/4 v6, 0x1

    if-eq v5, v6, :cond_4f

    iget-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w0:Z

    if-eqz v5, :cond_0

    goto/16 :goto_14

    :cond_0
    const-string v5, "Wallet (One-click Payment)"

    invoke-virtual {v2, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    iget v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/16 v8, 0xe

    if-lt v7, v8, :cond_1

    invoke-interface {v5}, Ljava/util/List;->isEmpty()Z

    move-result v5

    if-nez v5, :cond_1

    iput-boolean v6, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->s(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    return-void

    :cond_1
    iget v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q0:I

    const/4 v7, 0x6

    const-string v9, ""

    const/4 v10, 0x0

    if-nez v5, :cond_3

    iget-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z

    if-nez v5, :cond_3

    sget-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    iget-object v11, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J1:Ljava/lang/String;

    invoke-virtual {v5, v11}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    iget v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K1:I

    add-int/2addr v5, v6

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K1:I

    if-lt v5, v7, :cond_2

    iput v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K1:I

    return-void

    :cond_2
    sget-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    iput-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J1:Ljava/lang/String;

    sput-object v9, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-virtual {v0}, Landroid/accessibilityservice/AccessibilityService;->getRootInActiveWindow()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v5

    sput-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->g2:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-static {v2, v10}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    :cond_3
    const-string v5, "cris.org.in.prs.ima:id/booking_status"

    invoke-virtual {v2, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v11

    sget-boolean v12, Ljg;->h:Z

    if-eqz v12, :cond_4

    invoke-virtual {v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->j()V

    :cond_4
    const-string v12, "You are transferring"

    invoke-virtual {v2, v12}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v12

    const-string v13, "You are SENDING"

    invoke-virtual {v2, v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    const-string v14, "Enter your PIN"

    invoke-virtual {v2, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v14

    const-string v15, "BOOKING DETAILS"

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    invoke-interface {v15}, Ljava/util/List;->isEmpty()Z

    move-result v15

    const/16 v8, 0xd

    const/16 v7, 0xa

    if-nez v15, :cond_f

    const-string v1, "Booking Status"

    invoke-virtual {v2, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->isEmpty()Z

    move-result v5

    if-nez v5, :cond_4f

    iget-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w0:Z

    if-nez v5, :cond_4f

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v5

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11, v9}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v5, v7}, Ljava/util/Calendar;->get(I)I

    move-result v7

    invoke-virtual {v11, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const/16 v7, 0xc

    invoke-virtual {v5, v7}, Ljava/util/Calendar;->get(I)I

    move-result v12

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/util/Calendar;->get(I)I

    move-result v5

    invoke-virtual {v11, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    iput-boolean v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v:Z

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w0:Z

    const-string v8, "cris.org.in.prs.ima:id/pnr_no"

    invoke-virtual {v2, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v8

    invoke-interface {v8, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v8

    invoke-interface {v8}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v8

    const-string v11, "cris.org.in.prs.ima:id/tv_from_stationname"

    invoke-virtual {v2, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v11

    invoke-interface {v11, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v11

    invoke-interface {v11}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v11

    const-string v12, "\\("

    invoke-virtual {v11, v12}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v11

    aget-object v11, v11, v10

    invoke-virtual {v11}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v11

    const-string v13, "cris.org.in.prs.ima:id/tv_to_stationname"

    invoke-virtual {v2, v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    invoke-interface {v13, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v13

    check-cast v13, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v13

    invoke-interface {v13}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v13, v12}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v12

    aget-object v12, v12, v10

    invoke-virtual {v12}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v12

    const-string v13, "cris.org.in.prs.ima:id/travel_Time"

    invoke-virtual {v2, v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    invoke-interface {v13, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v13

    check-cast v13, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v13

    invoke-interface {v13}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v13

    const-string v14, "cris.org.in.prs.ima:id/selected_train_name"

    invoke-virtual {v2, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v14

    invoke-interface {v14, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v14

    invoke-interface {v14}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v14

    const-string v15, "cris.org.in.prs.ima:id/selected_train_number"

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    invoke-interface {v15, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v15

    check-cast v15, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v15

    invoke-interface {v15}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v15

    const/16 v20, 0x2

    const-string v6, "[^0-9]"

    invoke-virtual {v15, v6, v9}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    const-string v15, "cris.org.in.prs.ima:id/psgnCount_Class_Quota"

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    invoke-interface {v15, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v15

    check-cast v15, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v15

    invoke-interface {v15}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v15

    const-string v7, "\\|"

    invoke-virtual {v15, v7}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v16

    const/16 v19, 0x1

    aget-object v16, v16, v19

    invoke-virtual/range {v16 .. v16}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v15, v7}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v7

    aget-object v7, v7, v20

    invoke-virtual {v7}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v7

    const-string v15, "cris.org.in.prs.ima:id/total_fare"

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    move-object/from16 v22, v3

    const/4 v3, 0x0

    invoke-interface {v15, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v15

    check-cast v15, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v15

    invoke-interface {v15}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v15

    move-object/from16 v16, v5

    const-string v5, "cris.org.in.prs.ima:id/tv_dep_time"

    invoke-virtual {v2, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    invoke-interface {v5, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v5

    invoke-interface {v5}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v5

    move-object/from16 v23, v9

    const-string v9, "cris.org.in.prs.ima:id/tv_arv_timee"

    invoke-virtual {v2, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v9

    invoke-interface {v9, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v9

    check-cast v9, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v9

    invoke-interface {v9}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v9

    move-object/from16 v24, v4

    const-string v4, "cris.org.in.prs.ima:id/tv_dep_date"

    invoke-virtual {v2, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v4, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v4

    invoke-interface {v4}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v4

    move-object/from16 v25, v1

    const-string v1, "cris.org.in.prs.ima:id/tv_arv_date"

    invoke-virtual {v2, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    new-instance v3, Ljava/text/SimpleDateFormat;

    const-string v2, "dd-MMM-yy HH:mm:ss"

    move-object/from16 v26, v7

    sget-object v7, Ljava/util/Locale;->US:Ljava/util/Locale;

    invoke-direct {v3, v2, v7}, Ljava/text/SimpleDateFormat;-><init>(Ljava/lang/String;Ljava/util/Locale;)V

    new-instance v2, Ljava/util/Date;

    invoke-direct {v2}, Ljava/util/Date;-><init>()V

    invoke-virtual {v3, v2}, Ljava/text/DateFormat;->format(Ljava/util/Date;)Ljava/lang/String;

    move-result-object v2

    new-instance v3, Lyz0;

    invoke-direct {v3}, Lyz0;-><init>()V

    :try_start_0
    const-string v7, "pnr_no"

    invoke-virtual {v3, v8, v7}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v7, "from_stn"

    invoke-virtual {v3, v11, v7}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v7, "to_stn"

    invoke-virtual {v3, v12, v7}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v7, "journey_duration"

    invoke-virtual {v3, v13, v7}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v7, "train_name"

    invoke-virtual {v3, v14, v7}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v7, "train_no"

    invoke-virtual {v3, v6, v7}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v6, "booking_class"

    invoke-virtual {v3, v10, v6}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v6, "ticket_fare"

    invoke-virtual {v3, v15, v6}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v6, "irctc_id"

    iget-object v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->U1:Ljava/lang/String;

    invoke-virtual {v3, v7, v6}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v6, "departure_time"

    invoke-virtual {v3, v5, v6}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v5, "arrival_time"

    invoke-virtual {v3, v9, v5}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v5, "departure_date"

    invoke-virtual {v3, v4, v5}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v4, "arrival_date"

    invoke-virtual {v3, v1, v4}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v1, "booking_time"

    invoke-virtual {v3, v2, v1}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v1, "quota"

    move-object/from16 v2, v26

    invoke-virtual {v3, v2, v1}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    new-instance v1, Lwz0;

    invoke-direct {v1}, Lwz0;-><init>()V

    const-string v2, "cris.org.in.prs.ima:id/psgnList"

    move-object/from16 v4, p2

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    const/4 v4, 0x0

    invoke-interface {v2, v4}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/4 v4, 0x0

    :goto_0
    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v5

    if-ge v4, v5, :cond_6

    invoke-virtual {v2, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v5

    const-string v6, "cris.org.in.prs.ima:id/psgn_name"

    invoke-virtual {v5, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v6

    const/4 v7, 0x0

    invoke-interface {v6, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v6

    invoke-interface {v6}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v6

    const-string v7, "cris.org.in.prs.ima:id/tkt_status"

    invoke-virtual {v5, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    const/4 v7, 0x0

    invoke-interface {v5, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v5

    invoke-interface {v5}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v5

    const-string v7, "/"

    invoke-virtual {v5, v7}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v7

    array-length v8, v7

    const/4 v9, 0x3

    if-lt v8, v9, :cond_5

    const/16 v21, 0x0

    aget-object v8, v7, v21

    const/16 v19, 0x1

    aget-object v8, v7, v19

    aget-object v7, v7, v20

    :cond_5
    new-instance v7, Lyz0;

    invoke-direct {v7}, Lyz0;-><init>()V

    const-string v8, "name"

    invoke-virtual {v7, v6, v8}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v6, "booking_status"

    invoke-virtual {v7, v5, v6}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    invoke-virtual {v1, v7}, Lwz0;->put(Ljava/lang/Object;)V

    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    :cond_6
    const-string v2, "passengers"

    invoke-virtual {v3, v1, v2}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const/4 v1, 0x4

    invoke-virtual {v3, v1}, Lyz0;->H(I)Ljava/lang/String;

    move-result-object v1

    const-string v2, "IRCTC_DATA"

    invoke-static {v2, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {v0, v1}, Lag;->x(Landroid/content/ContextWrapper;Ljava/lang/String;)V
    :try_end_0
    .catch Lxz0; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    const/4 v7, 0x0

    iput-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    const-string v2, "Ticket Booked"

    invoke-virtual {v1, v2}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    move-object/from16 v1, v25

    :try_start_1
    invoke-interface {v1, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const/4 v3, 0x6

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    move-object v13, v1

    goto :goto_1

    :catch_1
    move-object/from16 v13, v22

    :goto_1
    new-instance v1, Lyz0;

    invoke-direct {v1}, Lyz0;-><init>()V

    :try_start_2
    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v3

    const/16 v4, 0xb

    invoke-virtual {v3, v4}, Ljava/util/Calendar;->get(I)I

    move-result v4

    const/16 v5, 0xc

    invoke-virtual {v3, v5}, Ljava/util/Calendar;->get(I)I

    move-result v3

    int-to-double v4, v4

    int-to-double v6, v3

    const-wide/high16 v8, 0x4059000000000000L    # 100.0

    div-double/2addr v6, v8

    add-double/2addr v6, v4

    const-string v3, "Source"

    const-string v4, "RC"

    invoke-virtual {v1, v4, v3}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v3, "Payment method"

    iget-boolean v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->O1:Z

    if-eqz v4, :cond_7

    const-string v4, "MANUAL"

    goto :goto_2

    :cond_7
    iget-object v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    :goto_2
    invoke-virtual {v1, v4, v3}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v3, "Booking status"

    sget-object v4, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    const-string v4, "RAC"

    const-string v5, "WL"
    :try_end_2
    .catch Lxz0; {:try_start_2 .. :try_end_2} :catch_2

    move-object/from16 v8, v24

    :try_start_3
    invoke-virtual {v13, v8}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v9

    if-eqz v9, :cond_8

    move-object v4, v8

    goto :goto_3

    :cond_8
    invoke-virtual {v13, v5}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v9

    if-eqz v9, :cond_9

    move-object v4, v5

    goto :goto_3

    :cond_9
    invoke-virtual {v13, v4}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v5

    if-eqz v5, :cond_a

    goto :goto_3

    :cond_a
    move-object/from16 v4, v22

    :goto_3
    invoke-virtual {v1, v4, v3}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v3, "Quota"

    iget-object v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    invoke-virtual {v1, v4, v3}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v3, "Bank"

    iget-object v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v1, v4, v3}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v3, "Time"

    invoke-static {v6, v7}, Ljava/lang/Double;->valueOf(D)Ljava/lang/Double;

    move-result-object v4

    invoke-virtual {v1, v4, v3}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    invoke-virtual {v3, v1, v2}, Lkf1;->l(Lyz0;Ljava/lang/String;)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    iget-object v1, v1, Lkf1;->f:Lu42;

    const-string v2, "Tickets Booked"

    const-wide/high16 v3, 0x3ff0000000000000L    # 1.0

    invoke-virtual {v1, v2, v3, v4}, Lu42;->u(Ljava/lang/String;D)V
    :try_end_3
    .catch Lxz0; {:try_start_3 .. :try_end_3} :catch_3

    goto :goto_4

    :catch_2
    move-object/from16 v8, v24

    :catch_3
    :goto_4
    invoke-virtual {v13, v8}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_c

    const/16 v19, 0x1

    sput-boolean v19, Lcom/tatkal/train/quick/FormActivity2;->C:Z

    sget-object v1, Ljg;->y:Ljava/lang/String;

    const-string v2, "FREE_USER"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_b

    sget-object v1, Ljg;->y:Ljava/lang/String;

    const-string v2, "COMP_USER"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_c

    :cond_b
    invoke-static {v0}, Lcom/tatkal/train/quick/HomeActivity;->A(Landroid/content/Context;)V

    :cond_c
    :try_start_4
    new-instance v1, Landroid/os/Bundle;

    invoke-direct {v1}, Landroid/os/Bundle;-><init>()V

    const-string v2, "value"

    const-string v3, "true"

    invoke-virtual {v1, v2, v3}, Landroid/os/BaseBundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

    invoke-static {v0}, Lcom/google/firebase/analytics/FirebaseAnalytics;->getInstance(Landroid/content/Context;)Lcom/google/firebase/analytics/FirebaseAnalytics;

    move-result-object v2

    const-string v3, "rc_ticket"

    invoke-virtual {v2, v1, v3}, Lcom/google/firebase/analytics/FirebaseAnalytics;->a(Landroid/os/Bundle;Ljava/lang/String;)V
    :try_end_4
    .catch Ljava/lang/Exception; {:try_start_4 .. :try_end_4} :catch_4

    :catch_4
    :try_start_5
    invoke-virtual {v13, v8}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_d

    const-string v1, "RATING"

    const/4 v7, 0x0

    invoke-virtual {v0, v1, v7}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v1

    invoke-interface {v1}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v1

    const-string v2, "VIEW"

    const/4 v8, 0x1

    invoke-interface {v1, v2, v8}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v1}, Landroid/content/SharedPreferences$Editor;->apply()V
    :try_end_5
    .catch Ljava/lang/Exception; {:try_start_5 .. :try_end_5} :catch_5

    :catch_5
    :cond_d
    iget-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y0:Z

    if-nez v1, :cond_4f

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y0:Z

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d0:Ljava/lang/String;

    const-string v2, "-"

    if-eqz v1, :cond_e

    invoke-virtual {v1, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_e

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d0:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v1

    aget-object v1, v1, v8

    invoke-virtual {v1}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v1

    :goto_5
    move-object v4, v1

    goto :goto_6

    :cond_e
    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v1

    aget-object v1, v1, v8

    invoke-virtual {v1}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v1

    goto :goto_5

    :goto_6
    new-instance v1, Lh82;

    new-instance v2, Ll82;

    invoke-direct {v2, v0}, Ll82;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;)V

    invoke-direct {v1, v2}, Lh82;-><init>(Ll82;)V

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b0:Ljava/lang/String;

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    const-string v6, "SOURCE_BOOKING"

    const-string v8, ""

    move-object v7, v13

    filled-new-array/range {v3 .. v8}, [Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Landroid/os/AsyncTask;->execute([Ljava/lang/Object;)Landroid/os/AsyncTask;

    new-instance v3, Ll82;

    invoke-direct {v3, v0}, Ll82;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;)V

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    iget-object v6, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    iget-object v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    iget-object v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b0:Ljava/lang/String;

    iget-object v9, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    iget-object v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    iget-object v11, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    iget-object v12, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->g0:Ljava/lang/String;

    move-object/from16 v0, v16

    move-object/from16 v2, v23

    invoke-virtual {v0, v2}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v14

    const-string v4, "RAILCONNECT"

    invoke-virtual/range {v3 .. v14}, Ll82;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    goto/16 :goto_14

    :cond_f
    move-object v4, v2

    move-object v2, v9

    const/16 v20, 0x2

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->g1:Z

    const/16 v6, 0x10

    if-eqz v3, :cond_1c

    sget-boolean v3, Ljg;->m:Z

    const-string v9, "Pay Securely"

    if-eqz v3, :cond_11

    const-string v3, "@com.phonepe.app:id/fl_proceed_bar"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v10, "Pay "

    invoke-virtual {v4, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v10

    const-string v15, "@net.one97.paytm:id/coordinator"

    invoke-virtual {v4, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    invoke-virtual {v4, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_10

    invoke-interface {v10}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_10

    const/4 v3, 0x0

    invoke-interface {v10, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    sput-boolean v3, Ljg;->m:Z

    return-void

    :cond_10
    const/4 v3, 0x0

    invoke-interface {v15}, Ljava/util/List;->size()I

    move-result v10

    if-lez v10, :cond_11

    sget-object v10, Lcom/tatkal/train/quick/MyAccessibilityService;->h2:Landroid/view/accessibility/AccessibilityNodeInfo;

    if-eqz v10, :cond_11

    invoke-virtual {v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    sput-boolean v3, Ljg;->m:Z

    return-void

    :cond_11
    sget-boolean v3, Ljg;->i:Z

    if-eqz v3, :cond_13

    invoke-interface {v12}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    invoke-interface {v13}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    invoke-interface {v14}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    const-string v3, "Requested by"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v9, "PROCEED TO PAY"

    invoke-virtual {v4, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v9

    const-string v10, "Total Payable"

    invoke-virtual {v4, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v10

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v3

    if-nez v3, :cond_12

    invoke-interface {v9}, Ljava/util/List;->size()I

    move-result v3

    if-nez v3, :cond_12

    invoke-interface {v10}, Ljava/util/List;->size()I

    move-result v3

    if-nez v3, :cond_12

    goto/16 :goto_14

    :cond_12
    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->q(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto/16 :goto_7

    :cond_13
    sget-boolean v3, Ljg;->j:Z

    if-eqz v3, :cond_18

    invoke-interface {v12}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    invoke-interface {v13}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    invoke-interface {v14}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    sput-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const/4 v3, 0x0

    invoke-static {v1, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const-string v9, ".Button"

    invoke-virtual {v3, v9}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    array-length v3, v3

    const/4 v9, 0x3

    if-ne v3, v9, :cond_4f

    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const-string v10, "android.widget.FrameLayout | 1"

    invoke-virtual {v3, v10}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v3

    if-nez v3, :cond_14

    goto/16 :goto_14

    :cond_14
    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const-string v10, ".FrameLayout"

    invoke-virtual {v3, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    array-length v3, v3

    if-le v3, v9, :cond_15

    goto/16 :goto_14

    :cond_15
    const/4 v3, 0x1

    iput-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A1:Z

    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const-string v9, ".View"

    invoke-virtual {v3, v9}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    array-length v3, v3

    const/16 v10, 0x14

    if-le v3, v10, :cond_16

    goto/16 :goto_14

    :cond_16
    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-virtual {v3, v9}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    array-length v3, v3

    if-le v3, v7, :cond_17

    sget-boolean v3, Ljg;->m:Z

    if-nez v3, :cond_17

    const/4 v3, 0x0

    iput-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A1:Z

    :cond_17
    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A1:Z

    if-eqz v3, :cond_1a

    invoke-virtual/range {p0 .. p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->i(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto :goto_7

    :cond_18
    sget-boolean v3, Ljg;->k:Z

    if-eqz v3, :cond_1a

    invoke-interface {v12}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    invoke-interface {v13}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    invoke-interface {v14}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_1a

    const-string v3, "Complete Payment to"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v10, "Decline Payment"

    invoke-virtual {v4, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v10

    invoke-virtual {v4, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v9

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v3

    if-nez v3, :cond_19

    invoke-interface {v10}, Ljava/util/List;->size()I

    move-result v3

    if-nez v3, :cond_19

    invoke-interface {v9}, Ljava/util/List;->size()I

    move-result v3

    if-nez v3, :cond_19

    goto/16 :goto_14

    :cond_19
    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->n(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    :cond_1a
    :goto_7
    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->G1:Z

    if-nez v3, :cond_1c

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->g1:Z

    if-eqz v3, :cond_1c

    invoke-interface {v12}, Ljava/util/List;->size()I

    move-result v3

    if-gtz v3, :cond_1b

    invoke-interface {v13}, Ljava/util/List;->size()I

    move-result v3

    if-gtz v3, :cond_1b

    invoke-interface {v14}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_1c

    :cond_1b
    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->x(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    :cond_1c
    sget-boolean v3, Ljg;->h:Z

    if-eqz v3, :cond_1d

    goto/16 :goto_14

    :cond_1d
    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q0:I

    if-lez v3, :cond_1f

    const/4 v9, 0x1

    if-ne v3, v9, :cond_1e

    move/from16 v1, v20

    invoke-virtual {v4, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const-string v2, "SUBMIT"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    iput-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->O0:Landroid/view/accessibility/AccessibilityNodeInfo;

    const/4 v7, 0x0

    invoke-interface {v2, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    iput-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->P0:Landroid/view/accessibility/AccessibilityNodeInfo;

    return-void

    :cond_1e
    move/from16 v1, v20

    const/4 v7, 0x0

    if-ne v3, v1, :cond_4f

    const-string v1, "cris.org.in.prs.ima:id/wallet_otp"

    invoke-virtual {v4, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const-string v2, "cris.org.in.prs.ima:id/proceed_to_payment"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v1, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    iput-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->O0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-interface {v2, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    iput-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->P0:Landroid/view/accessibility/AccessibilityNodeInfo;

    return-void

    :cond_1f
    const-string v3, "cris.org.in.prs.ima:id/progressStatus"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->isEmpty()Z

    move-result v3

    iget-object v9, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    if-nez v3, :cond_20

    const/16 v19, 0x1

    aput-boolean v19, v9, v19

    :cond_20
    :try_start_6
    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->e(Landroid/view/accessibility/AccessibilityNodeInfo;)Z

    move-result v3
    :try_end_6
    .catch Ljava/lang/Exception; {:try_start_6 .. :try_end_6} :catch_6

    if-eqz v3, :cond_21

    goto/16 :goto_14

    :catch_6
    :cond_21
    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    if-nez v3, :cond_22

    const-string v3, "PAY USING UPI"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-nez v3, :cond_22

    const/16 v3, 0xe

    iput v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v10, 0x1

    iput-boolean v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    goto :goto_8

    :cond_22
    const/16 v3, 0xe

    :goto_8
    iget v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    if-lt v10, v3, :cond_24

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    if-eqz v3, :cond_24

    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->c(Landroid/view/accessibility/AccessibilityNodeInfo;)Z

    move-result v1

    if-eqz v1, :cond_23

    goto/16 :goto_14

    :cond_23
    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->s(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    return-void

    :cond_24
    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->o1:Z

    if-eqz v3, :cond_25

    sput-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const/4 v3, 0x0

    invoke-static {v1, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    sget-object v3, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const-string v10, "Verified VPA ID"

    invoke-virtual {v3, v10}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v3

    if-nez v3, :cond_25

    goto/16 :goto_14

    :cond_25
    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->m1:Z

    if-eqz v3, :cond_26

    goto/16 :goto_14

    :cond_26
    const-string v3, "activity"

    invoke-virtual {v0, v3}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/app/ActivityManager;

    invoke-virtual {v3}, Landroid/app/ActivityManager;->getRunningAppProcesses()Ljava/util/List;

    move-result-object v3

    if-eqz v3, :cond_28

    invoke-interface {v3}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v3

    :cond_27
    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v10

    if-eqz v10, :cond_28

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v10

    check-cast v10, Landroid/app/ActivityManager$RunningAppProcessInfo;

    iget-object v10, v10, Landroid/app/ActivityManager$RunningAppProcessInfo;->processName:Ljava/lang/String;

    const-string v12, "com.google.android.gms"

    invoke-virtual {v10, v12}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v10

    if-eqz v10, :cond_27

    const/4 v10, 0x1

    iput v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v1:I

    return-void

    :cond_28
    const/4 v10, 0x1

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v1:I

    if-ne v3, v10, :cond_29

    const/4 v3, 0x2

    iput v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v1:I

    :cond_29
    sput-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const/4 v3, 0x0

    invoke-static {v4, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    const-string v10, "cris.org.in.prs.ima:id/tv_username"

    invoke-virtual {v4, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v12

    invoke-interface {v12}, Ljava/util/List;->isEmpty()Z

    move-result v13

    if-nez v13, :cond_2a

    :try_start_7
    invoke-interface {v12, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v13
    :try_end_7
    .catch Ljava/lang/Exception; {:try_start_7 .. :try_end_7} :catch_8

    :try_start_8
    check-cast v13, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    const-string v13, "for "

    invoke-virtual {v3, v13}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    const/16 v19, 0x1

    aget-object v3, v3, v19

    iput-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->U1:Ljava/lang/String;
    :try_end_8
    .catch Ljava/lang/Exception; {:try_start_8 .. :try_end_8} :catch_7

    goto :goto_9

    :catch_7
    const/4 v3, 0x0

    :catch_8
    invoke-interface {v12, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v12

    check-cast v12, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v12}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    iput-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->U1:Ljava/lang/String;

    :cond_2a
    :goto_9
    invoke-virtual {v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->j()V

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->V1:I

    const/4 v12, 0x1

    if-ne v3, v12, :cond_2b

    const-string v3, "cris.org.in.prs.ima:id/myprofile_ll"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->isEmpty()Z

    move-result v10

    if-nez v10, :cond_2c

    const/4 v12, 0x2

    iput v12, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->V1:I

    const/4 v13, 0x0

    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_2b
    const/4 v12, 0x2

    const/4 v13, 0x0

    if-ne v3, v12, :cond_2c

    invoke-virtual {v4, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->isEmpty()Z

    move-result v10

    if-nez v10, :cond_2c

    const/4 v10, 0x3

    iput v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->V1:I

    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    iput-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->U1:Ljava/lang/String;

    const-string v3, "cris.org.in.prs.ima:id/home_11"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->isEmpty()Z

    move-result v10

    if-nez v10, :cond_2c

    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_2c
    const-string v3, "tv_action_right1"

    invoke-virtual {v0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v10

    const-string v12, "et_valid_pin"

    invoke-virtual {v0, v12}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    const-string v14, "cris.org.in.prs.ima:id/tv_captcha_input"

    if-nez v13, :cond_2d

    invoke-virtual {v4, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    :cond_2d
    const-string v15, "my_journey_ll"

    invoke-virtual {v0, v15}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v16

    move/from16 v17, v7

    const-string v7, "Book Ticket"

    invoke-virtual {v4, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v7

    const-string v8, "Chart Vacancy"

    invoke-virtual {v4, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v8

    const-string v6, "cris.org.in.prs.ima:id/ll_from_station_layout"

    invoke-virtual {v4, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v6

    move-object/from16 v24, v2

    const-string v2, "cris.org.in.prs.ima:id/tv_search_text"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v25, v2

    const-string v2, "cris.org.in.prs.ima:id/lv_train_list"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v26, v2

    const-string v2, "tv_add_psgn_detail"

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v27, v2

    const-string v2, "cris.org.in.prs.ima:id/passenger_name"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v28, v2

    const-string v2, "cris.org.in.prs.ima:id/tv_infant_psgn_add"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v29, v2

    const-string v2, "SELECT A PAYMENT PROVIDER"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v30, v2

    const-string v2, "SELECT A PAYMENT METHOD"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move-object/from16 v31, v2

    const-string v2, "not a robot"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    const-string v2, "Select all images with"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    const-string v2, "SELECT POST"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_2e

    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->F0:I

    const/4 v2, 0x1

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E0:Z

    aput-boolean v2, v9, v17

    :cond_2e
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y1:Z

    if-eqz v2, :cond_30

    new-instance v2, Ljava/lang/StringBuilder;

    move-object/from16 v17, v6

    const-string v6, "AddnewViewId: "

    invoke-direct {v2, v6}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    if-eqz v27, :cond_2f

    invoke-interface/range {v27 .. v27}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_2f

    const/4 v6, 0x1

    goto :goto_a

    :cond_2f
    const/4 v6, 0x0

    :goto_a
    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    const-string v6, "STUDIOS"

    invoke-static {v6, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_b

    :cond_30
    move-object/from16 v17, v6

    :goto_b
    if-eqz v10, :cond_31

    invoke-interface {v10}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_31

    const/4 v2, 0x0

    aget-boolean v6, v9, v2

    if-nez v6, :cond_31

    invoke-virtual {v0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    if-eqz v1, :cond_4f

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_4f

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->C:Z

    if-nez v3, :cond_4f

    const/4 v8, 0x1

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->z0:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->s:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->v:Z

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->M:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->t:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y0:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w0:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D0:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E0:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->G0:Z

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H0:Z

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    const/16 v3, 0x63

    iput v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->F0:I

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->C:Z

    invoke-interface {v1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v3, 0x10

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    aput-boolean v8, v9, v2

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v2, "RC login auto click"

    invoke-virtual {v1, v2}, Lkf1;->m(Ljava/lang/String;)V

    new-instance v1, Landroid/os/Bundle;

    invoke-direct {v1}, Landroid/os/Bundle;-><init>()V

    const-string v2, "tickets"

    sget v3, Lcom/tatkal/train/quick/SplashActivity;->t:I

    invoke-virtual {v1, v2, v3}, Landroid/os/BaseBundle;->putInt(Ljava/lang/String;I)V

    invoke-static {v0}, Lcom/google/firebase/analytics/FirebaseAnalytics;->getInstance(Landroid/content/Context;)Lcom/google/firebase/analytics/FirebaseAnalytics;

    move-result-object v0

    const-string v2, "book_rc_login"

    invoke-virtual {v0, v1, v2}, Lcom/google/firebase/analytics/FirebaseAnalytics;->a(Landroid/os/Bundle;Ljava/lang/String;)V

    goto/16 :goto_14

    :cond_31
    const-string v2, "Decoding Captcha"

    if-eqz v13, :cond_37

    invoke-interface {v13}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_37

    sput-object v24, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const/4 v3, 0x0

    invoke-static {v4, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    invoke-virtual {v0, v12}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    if-nez v1, :cond_32

    invoke-virtual {v4, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    :cond_32
    const-string v3, "tv_captcha_input"

    invoke-virtual {v0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    if-eqz v1, :cond_33

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_33

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D:Z

    if-nez v1, :cond_33

    const/4 v1, 0x2

    iput v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D:Z

    new-instance v1, Landroid/os/Bundle;

    invoke-direct {v1}, Landroid/os/Bundle;-><init>()V

    const-string v6, "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE"

    iget-object v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->W:Ljava/lang/String;

    invoke-virtual {v1, v6, v7}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    :cond_33
    if-eqz v5, :cond_35

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_35

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->C0:Z

    if-eqz v1, :cond_35

    sget v1, Lcom/tatkal/train/quick/SplashActivity;->u:I

    const/4 v12, 0x2

    if-eq v1, v12, :cond_34

    goto :goto_c

    :cond_34
    sget v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I

    const/4 v8, 0x1

    if-ne v1, v8, :cond_4f

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-virtual {v1, v2}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    sput v8, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I

    const-wide/16 v1, 0x3e8

    :try_start_9
    invoke-static {v1, v2}, Ljava/lang/Thread;->sleep(J)V
    :try_end_9
    .catch Ljava/lang/Exception; {:try_start_9 .. :try_end_9} :catch_9

    :catch_9
    iput-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A0:Ljava/lang/String;

    const-string v1, "rl_pin_login"

    iput-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B0:Ljava/lang/String;

    new-instance v1, Landroid/graphics/Rect;

    const/4 v3, 0x0

    invoke-direct {v1, v3, v3, v3, v3}, Landroid/graphics/Rect;-><init>(IIII)V

    invoke-interface {v5, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getBoundsInScreen(Landroid/graphics/Rect;)V

    iget v2, v1, Landroid/graphics/Rect;->left:I

    sput v2, Lrt0;->f:I

    iget v2, v1, Landroid/graphics/Rect;->top:I

    sput v2, Lrt0;->g:I

    invoke-virtual {v1}, Landroid/graphics/Rect;->width()I

    move-result v2

    sput v2, Lrt0;->h:I

    invoke-virtual {v1}, Landroid/graphics/Rect;->height()I

    move-result v1

    sput v1, Lrt0;->i:I

    iget-object v0, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-virtual {v0}, Lcom/tatkal/train/quick/FloatingWidgetService;->d()V

    goto/16 :goto_14

    :cond_35
    :goto_c
    if-eqz v5, :cond_36

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v1

    if-nez v1, :cond_4f

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->G0:Z

    if-nez v1, :cond_4f

    :cond_36
    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->G0:Z

    const-string v0, "cris.org.in.prs.ima:id/rl_pin_login"

    invoke-virtual {v4, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    goto/16 :goto_14

    :cond_37
    if-eqz v16, :cond_3a

    invoke-interface/range {v16 .. v16}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_3a

    :try_start_a
    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-virtual {v1}, Lcom/tatkal/train/quick/FloatingWidgetService;->b()V
    :try_end_a
    .catch Ljava/lang/Exception; {:try_start_a .. :try_end_a} :catch_a

    :catch_a
    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->V1:I

    if-nez v1, :cond_38

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->U1:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->isEmpty()Z

    move-result v1

    if-eqz v1, :cond_38

    const-string v1, "cris.org.in.prs.ima:id/myaccount_ll"

    invoke-virtual {v4, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->isEmpty()Z

    move-result v2

    if-nez v2, :cond_38

    const/4 v8, 0x1

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->V1:I

    const/4 v3, 0x0

    invoke-interface {v1, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v3, 0x10

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_38
    const/16 v18, 0x4

    sput v18, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I

    invoke-virtual {v0, v15}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    if-eqz v1, :cond_4f

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_4f

    const/4 v10, 0x3

    iput v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    sget-object v2, Landroid/os/Environment;->DIRECTORY_DOCUMENTS:Ljava/lang/String;

    invoke-virtual {v0, v2}, Landroid/content/Context;->getExternalFilesDir(Ljava/lang/String;)Ljava/io/File;

    move-result-object v2

    if-eqz v2, :cond_39

    invoke-virtual {v2}, Ljava/io/File;->exists()Z

    move-result v3

    if-nez v3, :cond_39

    invoke-virtual {v2}, Ljava/io/File;->mkdirs()Z

    :cond_39
    new-instance v3, Ljava/io/File;

    const-string v4, "payment"

    invoke-direct {v3, v2, v4}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    :try_start_b
    invoke-virtual {v3}, Ljava/io/File;->createNewFile()Z
    :try_end_b
    .catch Ljava/io/IOException; {:try_start_b .. :try_end_b} :catch_b

    goto :goto_d

    :catch_b
    invoke-virtual {v3}, Ljava/io/File;->delete()Z

    :goto_d
    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    const-string v3, "Filling journey details"

    invoke-virtual {v2, v3}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    const/4 v3, 0x0

    iput-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->C:Z

    iput-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D:Z

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->z0:Z

    invoke-interface {v1, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v3, 0x10

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/16 v20, 0x2

    aput-boolean v8, v9, v20

    goto/16 :goto_14

    :cond_3a
    if-eqz v8, :cond_3d

    invoke-interface {v8}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_3d

    const/4 v1, 0x0

    :goto_e
    invoke-interface {v7}, Ljava/util/List;->size()I

    move-result v2

    if-ge v1, v2, :cond_3c

    invoke-interface {v7, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v2

    invoke-interface {v2}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/String;->toLowerCase()Ljava/lang/String;

    move-result-object v2

    const-string v3, "textview"

    invoke-virtual {v2, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v2

    if-eqz v2, :cond_3b

    move v10, v1

    goto :goto_f

    :cond_3b
    add-int/lit8 v1, v1, 0x1

    goto :goto_e

    :cond_3c
    const/4 v10, 0x0

    :goto_f
    invoke-interface {v7, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const/16 v3, 0x10

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget-object v0, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v1, "RC Book Ticket click"

    invoke-virtual {v0, v1}, Lkf1;->m(Ljava/lang/String;)V

    goto/16 :goto_14

    :cond_3d
    invoke-interface/range {v17 .. v17}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_4e

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I0:Z

    if-eqz v3, :cond_3e

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    if-eqz v3, :cond_4e

    :cond_3e
    invoke-interface/range {v25 .. v25}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_3f

    goto/16 :goto_13

    :cond_3f
    invoke-interface/range {v26 .. v26}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-nez v3, :cond_40

    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->v(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto/16 :goto_14

    :cond_40
    if-eqz v27, :cond_43

    invoke-interface/range {v27 .. v27}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-nez v3, :cond_43

    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w1:Z

    if-nez v2, :cond_41

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w1:Z

    :cond_41
    const-string v2, "cris.org.in.prs.ima:id/tv_masterpass_cancel"

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-nez v3, :cond_42

    const/4 v3, 0x0

    invoke-interface {v2, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v3, 0x10

    invoke-virtual {v2, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_42
    invoke-virtual {v0, v4, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->l(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto/16 :goto_14

    :cond_43
    invoke-interface/range {v28 .. v28}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_4d

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->F0:I

    const/4 v12, 0x2

    if-eq v3, v12, :cond_4d

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    const/4 v8, 0x1

    if-eq v3, v8, :cond_4d

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    if-eq v3, v8, :cond_4d

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    if-ne v3, v8, :cond_44

    goto/16 :goto_12

    :cond_44
    invoke-interface/range {v29 .. v29}, Ljava/util/List;->isEmpty()Z

    move-result v3

    if-eqz v3, :cond_4c

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->F0:I

    if-nez v3, :cond_45

    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E0:Z

    if-eqz v3, :cond_45

    goto/16 :goto_11

    :cond_45
    invoke-interface {v11}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_4a

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const-string v3, "cris.org.in.prs.ima:id/captcha"

    invoke-virtual {v4, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_49

    iget-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->M:Z

    if-nez v5, :cond_49

    const/16 v5, 0xd

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v7, 0x0

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iput-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    iput-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    iput-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    iput-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->F0:I

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->M:Z

    if-eqz v3, :cond_47

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_47

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    iget v6, v5, Lcom/tatkal/train/quick/FloatingWidgetService;->F:I

    sget v6, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I

    const/4 v7, 0x4

    if-ne v6, v7, :cond_48

    iget-boolean v6, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->C0:Z

    if-eqz v6, :cond_48

    sget v6, Lcom/tatkal/train/quick/SplashActivity;->u:I

    const/4 v12, 0x2

    if-eq v6, v12, :cond_46

    goto :goto_10

    :cond_46
    invoke-virtual {v5, v2}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    const/16 v19, 0x1

    sput v19, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I

    sput v19, Lcom/tatkal/train/quick/MyAccessibilityService;->f2:I

    const-string v2, "captcha_input"

    iput-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A0:Ljava/lang/String;

    const-string v5, "make_payment"

    iput-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B0:Ljava/lang/String;

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    new-instance v2, Landroid/graphics/Rect;

    const/4 v7, 0x0

    invoke-direct {v2, v7, v7, v7, v7}, Landroid/graphics/Rect;-><init>(IIII)V

    invoke-interface {v3, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getBoundsInScreen(Landroid/graphics/Rect;)V

    iget v3, v2, Landroid/graphics/Rect;->left:I

    sput v3, Lrt0;->f:I

    iget v3, v2, Landroid/graphics/Rect;->top:I

    sput v3, Lrt0;->g:I

    invoke-virtual {v2}, Landroid/graphics/Rect;->width()I

    move-result v3

    sput v3, Lrt0;->h:I

    invoke-virtual {v2}, Landroid/graphics/Rect;->height()I

    move-result v2

    sput v2, Lrt0;->i:I

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-virtual {v2}, Lcom/tatkal/train/quick/FloatingWidgetService;->d()V

    goto :goto_10

    :cond_47
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H0:Z

    if-nez v2, :cond_48

    const/4 v8, 0x1

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H0:Z

    :cond_48
    :goto_10
    iget-object v0, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    const/4 v3, 0x0

    invoke-interface {v1, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    :cond_49
    const-string v0, "cris.org.in.prs.ima:id/make_payment"

    invoke-virtual {v4, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    goto :goto_14

    :cond_4a
    invoke-interface/range {v30 .. v30}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-eqz v1, :cond_4b

    invoke-interface/range {v31 .. v31}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_4f

    :cond_4b
    :try_start_c
    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-virtual {v1}, Lcom/tatkal/train/quick/FloatingWidgetService;->b()V
    :try_end_c
    .catch Ljava/lang/Exception; {:try_start_c .. :try_end_c} :catch_c

    :catch_c
    invoke-virtual {v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->m(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto :goto_14

    :cond_4c
    :goto_11
    invoke-virtual {v0, v4, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->l(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto :goto_14

    :cond_4d
    :goto_12
    invoke-virtual {v0, v4, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->l(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V

    goto :goto_14

    :cond_4e
    :goto_13
    invoke-virtual {v0, v4, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->u(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V

    :cond_4f
    :goto_14
    return-void
.end method

.method public final c(Landroid/view/accessibility/AccessibilityNodeInfo;)Z
    .locals 5

    const-string v0, ""

    iget v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->N1:I

    const/4 v2, 0x0

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    const-string v1, "SELECT A PAYMENT PROVIDER"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const-string v3, "SELECT A PAYMENT METHOD"

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-gtz v1, :cond_1

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_2

    :cond_1
    const-string v1, "\u20b9"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    :try_start_0
    invoke-interface {p1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4, v0}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3, v1, v0}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Double;->parseDouble(Ljava/lang/String;)D

    move-result-wide v0

    invoke-static {v0, v1}, Ljava/lang/Math;->floor(D)D

    move-result-wide v0

    double-to-int v0, v0

    iget v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a0:I

    if-lez v1, :cond_2

    if-lt v0, v1, :cond_2

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-interface {p1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object p1

    invoke-interface {p1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object p1

    iget-object v1, v0, Lcom/tatkal/train/quick/FloatingWidgetService;->s:Landroid/widget/LinearLayout;

    invoke-virtual {v1, v2}, Landroid/view/View;->setVisibility(I)V

    iget-object v0, v0, Lcom/tatkal/train/quick/FloatingWidgetService;->t:Landroid/widget/TextView;

    new-instance v1, Ljava/lang/StringBuilder;

    const-string v3, "Total fare for ticket is "

    invoke-direct {v1, v3}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ". Continue with payment?"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    const/4 p1, 0x1

    iput p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->N1:I
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    return p1

    :catch_0
    :cond_2
    :goto_0
    return v2
.end method

.method public final e(Landroid/view/accessibility/AccessibilityNodeInfo;)Z
    .locals 10

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->y1:Z

    const-string v1, "STUDIOS"

    if-eqz v0, :cond_0

    const-string v0, "INTO DISMISS ALERTS"

    invoke-static {v1, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :cond_0
    const-string v0, "will not be provided for any"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    const-string v2, "YES"

    const/16 v3, 0x10

    const/4 v4, 0x1

    if-lez v0, :cond_1

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_1
    const-string v0, "not a robot"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v5, "Select all images with"

    invoke-virtual {p1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v5

    if-gtz v5, :cond_17

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_2

    goto/16 :goto_4

    :cond_2
    const-string v0, "Error"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    const/4 v5, 0x0

    if-lez v0, :cond_3

    const-string p0, "TRY AGAIN"

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_3
    const-string v0, "PNR"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    iget-boolean v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    if-eqz v6, :cond_4

    iget v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/16 v7, 0xe

    if-lt v6, v7, :cond_4

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_4

    const-string v0, "DECLINE"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_4

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_4
    const-string v0, "Your last transaction Status is"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_5

    const-string p1, "my_journey_ll"

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    if-eqz p1, :cond_17

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_17

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z0:Z

    return v4

    :cond_5
    const-string v0, "Full fare will be charged"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v6, "Authenticate User"

    invoke-virtual {p1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v6

    const-string v7, "With effect from"

    invoke-virtual {p1, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v7

    const-string v8, "only Aadhaar-authenticated"

    invoke-virtual {p1, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v8

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    const-string v9, "OK"

    if-gtz v0, :cond_16

    invoke-interface {v6}, Ljava/util/List;->size()I

    move-result v0

    if-gtz v0, :cond_16

    invoke-interface {v7}, Ljava/util/List;->size()I

    move-result v0

    if-gtz v0, :cond_16

    invoke-interface {v8}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_6

    goto/16 :goto_3

    :cond_6
    const-string v0, "Onboard Catering"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_7

    const-string p0, "NOT INTERESTED"

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_7
    const-string v0, "Verify Passengers"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_8

    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_8
    const-string v0, "Specially abled and Journalist passengers availing"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v6, "What is LOWER BERTH"

    invoke-virtual {p1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v6

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-gtz v0, :cond_15

    invoke-interface {v6}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_9

    goto/16 :goto_2

    :cond_9
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->y1:Z

    if-eqz v0, :cond_a

    const-string v0, "CHECKING SENIOR ALERT"

    invoke-static {v1, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :cond_a
    const-string v0, "Senior Citizen concession not allowed"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_c

    iget-boolean p0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->y1:Z

    if-eqz p0, :cond_b

    const-string p0, "SENIOR ALERT FOUND"

    invoke-static {v1, p0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :cond_b
    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_c
    const-string v0, "No normal passenger other than"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v6, "Dynamic Pricing is applicable"

    invoke-virtual {p1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v6

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-gtz v0, :cond_14

    invoke-interface {v6}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_d

    goto/16 :goto_1

    :cond_d
    const-string v0, "You have searched trains for"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_e

    const-string p0, "CONFIRM"

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_e
    const-string v0, "I AGREE"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_f

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->y1:Z

    const-string p1, "I AGREE CLICKED"

    invoke-static {v1, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput-boolean v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z

    return v4

    :cond_f
    const-string p0, "There are Maximum Waiting List"

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    if-lez p0, :cond_10

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_10

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_10
    const-string p0, "No concessional tickets allowed for this"

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    const-string v0, "Full fare will be applicable in case"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    if-gtz p0, :cond_13

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result p0

    if-lez p0, :cond_11

    goto :goto_0

    :cond_11
    const-string p0, "Passengers may get berth allotted in"

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    if-lez p0, :cond_12

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_12
    return v5

    :cond_13
    :goto_0
    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_14
    :goto_1
    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_15
    :goto_2
    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return v4

    :cond_16
    :goto_3
    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-static {v4, p0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_17
    :goto_4
    return v4
.end method

.method public final f(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;)Landroid/view/accessibility/AccessibilityNodeInfo;
    .locals 7

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v1

    const/4 v2, 0x0

    if-eqz v1, :cond_1

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1, p2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_1

    iget v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W1:I

    if-nez v1, :cond_0

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W1:I

    return-object p1

    :cond_0
    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W1:I

    :cond_1
    move v1, v2

    :goto_0
    if-ge v1, v0, :cond_7

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v4

    if-nez v4, :cond_2

    goto :goto_2

    :cond_2
    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v4

    invoke-interface {v4}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v5

    invoke-virtual {v4, p2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v4

    if-eqz v4, :cond_4

    iget v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W1:I

    if-nez v4, :cond_3

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W1:I

    return-object v3

    :cond_3
    add-int/lit8 v4, v4, 0x1

    iput v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->W1:I

    :cond_4
    if-lez v5, :cond_6

    move v4, v2

    :goto_1
    if-ge v4, v5, :cond_6

    invoke-virtual {v3, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {p0, v6, p2}, Lcom/tatkal/train/quick/MyAccessibilityService;->f(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    if-eqz v6, :cond_5

    return-object v6

    :cond_5
    add-int/lit8 v4, v4, 0x1

    goto :goto_1

    :cond_6
    :goto_2
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_7
    const/4 p0, 0x0

    return-object p0
.end method

.method public final g(Ljava/lang/String;)Ljava/util/List;
    .locals 4

    invoke-virtual {p0}, Landroid/accessibilityservice/AccessibilityService;->getWindows()Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :cond_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityWindowInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityWindowInfo;->getRoot()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    if-eqz v1, :cond_0

    const-string v2, "captcha_input"

    invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_1

    new-instance v2, Landroid/graphics/Rect;

    const/4 v3, 0x0

    invoke-direct {v2, v3, v3, v3, v3}, Landroid/graphics/Rect;-><init>(IIII)V

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityWindowInfo;->getBoundsInScreen(Landroid/graphics/Rect;)V

    iget v0, v2, Landroid/graphics/Rect;->right:I

    sput v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b2:I

    iget v0, v2, Landroid/graphics/Rect;->bottom:I

    sput v0, Lcom/tatkal/train/quick/MyAccessibilityService;->c2:I

    :cond_1
    const-string v0, "cris.org.in.prs.ima:id/"

    invoke-virtual {v0, p1}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_0

    return-object v0

    :cond_2
    const/4 p0, 0x0

    return-object p0
.end method

.method public final i(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 10

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, "android.widget.Button"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    const/16 v3, 0x10

    const/4 v4, 0x0

    const/4 v5, 0x1

    if-eqz v1, :cond_1

    iget-boolean v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z1:Z

    if-nez v1, :cond_0

    iput-boolean v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z1:Z

    goto :goto_0

    :cond_0
    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z1:Z

    :cond_1
    :goto_0
    move v1, v4

    :goto_1
    if-ge v1, v0, :cond_5

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v7

    invoke-interface {v7}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v7, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v7

    if-eqz v7, :cond_3

    iget-boolean v7, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z1:Z

    if-nez v7, :cond_2

    iput-boolean v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z1:Z

    goto :goto_2

    :cond_2
    invoke-virtual {v6, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->z1:Z

    :cond_3
    :goto_2
    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v7

    if-lez v7, :cond_4

    move v8, v4

    :goto_3
    if-ge v8, v7, :cond_4

    invoke-virtual {v6, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v9

    invoke-virtual {p0, v9}, Lcom/tatkal/train/quick/MyAccessibilityService;->i(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    add-int/lit8 v8, v8, 0x1

    goto :goto_3

    :cond_4
    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    :cond_5
    return-void
.end method

.method public final j()V
    .locals 21

    move-object/from16 v1, p0

    sget-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    const/4 v2, 0x1

    if-nez v0, :cond_d

    :try_start_0
    new-instance v0, Landroid/os/Bundle;

    invoke-direct {v0}, Landroid/os/Bundle;-><init>()V

    const-string v3, "value"

    const-string v4, "true"

    invoke-virtual {v0, v3, v4}, Landroid/os/BaseBundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

    invoke-static {v1}, Lcom/google/firebase/analytics/FirebaseAnalytics;->getInstance(Landroid/content/Context;)Lcom/google/firebase/analytics/FirebaseAnalytics;

    move-result-object v3

    const-string v4, "rc_opened"

    invoke-virtual {v3, v0, v4}, Lcom/google/firebase/analytics/FirebaseAnalytics;->a(Landroid/os/Bundle;Ljava/lang/String;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v3, Lvw1;->overlay_appear:I

    invoke-virtual {v0, v3}, Landroid/content/res/Resources;->openRawResourceFd(I)Landroid/content/res/AssetFileDescriptor;

    move-result-object v0

    const/4 v3, 0x5

    :try_start_1
    new-instance v4, Landroid/media/MediaPlayer;

    invoke-direct {v4}, Landroid/media/MediaPlayer;-><init>()V

    invoke-virtual {v4, v3}, Landroid/media/MediaPlayer;->setAudioStreamType(I)V

    invoke-virtual {v0}, Landroid/content/res/AssetFileDescriptor;->getFileDescriptor()Ljava/io/FileDescriptor;

    move-result-object v5

    invoke-virtual {v0}, Landroid/content/res/AssetFileDescriptor;->getStartOffset()J

    move-result-wide v6

    invoke-virtual {v0}, Landroid/content/res/AssetFileDescriptor;->getLength()J

    move-result-wide v8

    invoke-virtual/range {v4 .. v9}, Landroid/media/MediaPlayer;->setDataSource(Ljava/io/FileDescriptor;JJ)V

    invoke-virtual {v4}, Landroid/media/MediaPlayer;->prepare()V

    invoke-virtual {v4}, Landroid/media/MediaPlayer;->start()V
    :try_end_1
    .catch Ljava/io/IOException; {:try_start_1 .. :try_end_1} :catch_1

    goto :goto_0

    :catch_1
    move-exception v0

    invoke-virtual {v0}, Ljava/lang/Throwable;->printStackTrace()V

    :goto_0
    const-string v0, "SL"

    const-string v4, "Sleeper"

    iget-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s0:Ljava/util/HashMap;

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "3A"

    const-string v4, "AC 3 Tier"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "2A"

    const-string v4, "AC 2 Tier"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "1A"

    const-string v4, "AC First Class"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "EC"

    const-string v4, "Exec. Chair Car"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "FC"

    const-string v4, "First Class"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "3E"

    const-string v4, "AC 3 Economy"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "2S"

    const-string v4, "Second Sitting"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "CC"

    const-string v4, "AC Chair car"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "EA"

    const-string v4, "Anubhuti Class"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "VS"

    const-string v4, "Vistadome Non AC"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "VC"

    const-string v4, "Vistadome Chair car"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v0, "EV"

    const-string v4, "Vistadome AC"

    invoke-virtual {v5, v0, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v1}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    const-string v4, "RC"

    const/4 v5, 0x0

    invoke-virtual {v0, v4, v5}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v0

    const-string v4, "FORM_NAME"

    const-string v6, ""

    invoke-interface {v0, v4, v6}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    new-instance v0, Lz3;

    invoke-direct {v0, v1, v5}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v4

    new-instance v6, Ljava/lang/StringBuilder;

    const-string v7, "select * from ADDRESS_TBL where FORM_NAME = \'"

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v7, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    const-string v8, "\'"

    const/4 v9, 0x0

    invoke-static {v6, v7, v8, v4, v9}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v6

    invoke-interface {v6}, Landroid/database/Cursor;->moveToNext()Z

    move-result v7

    if-eqz v7, :cond_0

    const-string v7, "ADDR1"

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v7, "ADDR2"

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v7, "ADDR3"

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v7, "PIN"

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v7, "CITY"

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v7, "PO"

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v6, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    :cond_0
    invoke-interface {v6}, Landroid/database/Cursor;->close()V

    invoke-virtual {v4}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    new-instance v0, Lag;

    const/4 v4, 0x2

    invoke-direct {v0, v1, v4}, Lag;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v0

    new-instance v6, Ljava/lang/StringBuilder;

    const-string v7, "select * from BOOKING_INFO where FORM_NAME = \'"

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v7, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    invoke-static {v6, v7, v8, v0, v9}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v0

    invoke-static {}, Lzo0;->g()[B

    move-result-object v6

    invoke-interface {v0}, Landroid/database/Cursor;->moveToNext()Z

    move-result v7

    const/4 v10, 0x7

    if-eqz v7, :cond_a

    const-string v7, "RC_PIN"

    invoke-static {v0, v7, v6}, Lp91;->q(Landroid/database/Cursor;Ljava/lang/String;[B)[B

    move-result-object v7

    new-instance v11, Ljava/lang/String;

    const-string v12, "UTF8"

    invoke-static {v12}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object v12

    invoke-direct {v11, v7, v12}, Ljava/lang/String;-><init>([BLjava/nio/charset/Charset;)V

    iput-object v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->W:Ljava/lang/String;

    const/4 v7, 0x3

    invoke-interface {v0, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v11

    iput-object v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    const/4 v11, 0x4

    invoke-interface {v0, v11}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    invoke-interface {v0, v3}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    const/4 v12, 0x6

    invoke-interface {v0, v12}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    :try_start_2
    const-string v13, "FARE_LIMIT"

    invoke-interface {v0, v13}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v13

    invoke-interface {v0, v13}, Landroid/database/Cursor;->getInt(I)I

    move-result v13

    iput v13, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a0:I
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_2

    :catch_2
    const-string v13, "CLICK"

    invoke-virtual {v1, v13, v5}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v13

    const-string v14, "VALUE"

    invoke-interface {v13, v14, v5}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v13

    iput v13, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M0:I

    invoke-interface {v0, v10}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    const/16 v13, 0x8

    invoke-interface {v0, v13}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v14

    iput-object v14, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b0:Ljava/lang/String;

    const/16 v14, 0x9

    invoke-interface {v0, v14}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v15

    iput-object v15, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d0:Ljava/lang/String;

    const/16 v15, 0xa

    invoke-interface {v0, v15}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    iput-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->p0:Ljava/lang/String;

    const/16 v5, 0xb

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v17

    invoke-static/range {v17 .. v17}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v5

    iput-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->o0:Z

    const/16 v5, 0xc

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v17

    invoke-static/range {v17 .. v17}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    const/16 v5, 0xd

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v18

    invoke-static/range {v18 .. v18}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v5

    iput-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->j0:Z

    const-string v5, "NO_FOOD"

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getInt(I)I

    move-result v5

    if-ne v5, v2, :cond_1

    move v5, v2

    goto :goto_1

    :cond_1
    const/4 v5, 0x0

    :goto_1
    iput-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->i0:Z

    const/16 v5, 0xe

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v18

    invoke-static/range {v18 .. v18}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v5

    iput-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->k0:Z

    :try_start_3
    const-string v5, "WB_PYMT_MODE"

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v5

    iput v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->m0:I
    :try_end_3
    .catch Ljava/lang/Exception; {:try_start_3 .. :try_end_3} :catch_3

    :catch_3
    const/16 v5, 0xf

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getInt(I)I

    move-result v5

    iput v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->n0:I

    const/16 v5, 0x10

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    iput-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->g0:Ljava/lang/String;

    const-string v5, "PYMT_AUTO_RC"

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getInt(I)I

    move-result v5

    if-ne v5, v2, :cond_2

    move v5, v2

    goto :goto_2

    :cond_2
    const/4 v5, 0x0

    :goto_2
    iput-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->O1:Z

    const-string v5, "CAPTCHA_AUTOFILL"

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    const-string v15, "1"

    invoke-virtual {v5, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_3

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->C0:Z

    sget-object v5, Lk6;->r:Ljava/lang/String;

    const-string v14, "N"

    invoke-virtual {v5, v14}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    :cond_3
    const-string v5, "AUTO_OPEN"

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_4

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f1:Z

    :cond_4
    const-string v5, "UPI_AUTOMATE_RC"

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_5

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->g1:Z

    :cond_5
    const/16 v5, 0x30

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getInt(I)I

    move-result v5

    iput v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->q0:I

    const/16 v5, 0x31

    invoke-interface {v0, v5}, Landroid/database/Cursor;->getInt(I)I

    move-result v0

    iput v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->r0:I

    iget v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->q0:I

    new-array v0, v0, [Ldo1;

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    new-instance v0, Lz3;

    invoke-direct {v0, v1, v3}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v5

    new-instance v14, Ljava/lang/StringBuilder;

    const-string v15, "select * from PASSENGER_INFO where FORM_NAME = \'"

    invoke-direct {v14, v15}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v15, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    invoke-static {v14, v15, v8, v5, v9}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v14

    const/4 v15, 0x0

    :goto_3
    invoke-interface {v14}, Landroid/database/Cursor;->moveToNext()Z

    move-result v20

    if-eqz v20, :cond_7

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    new-instance v13, Ldo1;

    invoke-direct {v13}, Ljava/lang/Object;-><init>()V

    aput-object v13, v9, v15

    invoke-interface {v14, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v13, Ldo1;->j:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    invoke-interface {v14, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v9, Ldo1;->g:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    invoke-interface {v14, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v9, Ldo1;->i:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    invoke-interface {v14, v11}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v9, Ldo1;->l:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    invoke-interface {v14, v3}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v9, Ldo1;->m:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    invoke-interface {v14, v12}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v9, Ldo1;->n:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    invoke-interface {v14, v10}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v9, Ldo1;->a:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    const/16 v13, 0x8

    invoke-interface {v14, v13}, Landroid/database/Cursor;->getInt(I)I

    move-result v3

    iput v3, v9, Ldo1;->b:I

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v3, v3, v15

    const/16 v9, 0x9

    invoke-interface {v14, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v3, Ldo1;->c:Ljava/lang/String;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v3, v3, v15

    const/16 v12, 0xa

    invoke-interface {v14, v12}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v3, Ldo1;->d:Ljava/lang/String;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v3, v3, v15

    const-string v9, "OPT_BERTH"

    invoke-interface {v14, v9}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v9

    invoke-interface {v14, v9}, Landroid/database/Cursor;->getInt(I)I

    move-result v9

    iput v9, v3, Ldo1;->o:I

    const/16 v3, 0xb

    invoke-interface {v14, v3}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    const-string v12, "NA"

    invoke-virtual {v9, v12}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v9

    iget-object v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    if-nez v9, :cond_6

    aget-object v9, v12, v15

    invoke-interface {v14, v3}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v9, Ldo1;->f:Ljava/lang/String;

    goto :goto_4

    :cond_6
    aget-object v9, v12, v15

    const-string v12, "V"

    iput-object v12, v9, Ldo1;->f:Ljava/lang/String;

    :goto_4
    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v9, v9, v15

    const/16 v12, 0xc

    invoke-interface {v14, v12}, Landroid/database/Cursor;->getInt(I)I

    move-result v3

    iput v3, v9, Ldo1;->e:I

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v3, v3, v15

    const/16 v9, 0xd

    invoke-interface {v14, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v19

    invoke-static/range {v19 .. v19}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v9

    iput-boolean v9, v3, Ldo1;->h:Z

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    aget-object v3, v3, v15

    const/16 v9, 0xe

    invoke-interface {v14, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v3, Ldo1;->k:Ljava/lang/String;

    add-int/lit8 v15, v15, 0x1

    const/4 v3, 0x5

    const/4 v9, 0x0

    const/4 v12, 0x6

    goto/16 :goto_3

    :cond_7
    invoke-interface {v14}, Landroid/database/Cursor;->close()V

    invoke-virtual {v5}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    iget v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->r0:I

    new-array v0, v0, [Lrl;

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    new-instance v0, Lz3;

    invoke-direct {v0, v1, v2}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v3

    new-instance v5, Ljava/lang/StringBuilder;

    const-string v9, "select * from CHILD_INFO where FORM_NAME = \'"

    invoke-direct {v5, v9}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v9, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    const/4 v12, 0x0

    invoke-static {v5, v9, v8, v3, v12}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v5

    const/16 v16, 0x0

    :goto_5
    invoke-interface {v5}, Landroid/database/Cursor;->moveToNext()Z

    move-result v9

    if-eqz v9, :cond_8

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    new-instance v12, Lrl;

    invoke-direct {v12}, Ljava/lang/Object;-><init>()V

    aput-object v12, v9, v16

    invoke-interface {v5, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v12, Lrl;->a:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    aget-object v9, v9, v16

    invoke-interface {v5, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v9, Lrl;->b:Ljava/lang/String;

    iget-object v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    aget-object v9, v9, v16

    invoke-interface {v5, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v9, Lrl;->c:Ljava/lang/String;

    add-int/lit8 v16, v16, 0x1

    goto :goto_5

    :cond_8
    invoke-interface {v5}, Landroid/database/Cursor;->close()V

    invoke-virtual {v3}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    new-instance v0, Lz3;

    invoke-direct {v0, v1, v11}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    const-string v5, "select * from INSURANCE where FORM_NAME = \'"

    invoke-direct {v4, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    const/4 v12, 0x0

    invoke-static {v4, v5, v8, v3, v12}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v4

    invoke-interface {v4}, Landroid/database/Cursor;->moveToNext()Z

    move-result v5

    if-eqz v5, :cond_9

    invoke-interface {v4, v2}, Landroid/database/Cursor;->getInt(I)I

    move-result v5

    iput v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->h0:I

    :cond_9
    invoke-interface {v4}, Landroid/database/Cursor;->close()V

    invoke-virtual {v3}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    :cond_a
    new-instance v0, Lz3;

    invoke-direct {v0, v1, v10}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    const-string v5, "select * from RC_PYMT_INFO where FORM_NAME = \'"

    invoke-direct {v4, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    const/4 v12, 0x0

    invoke-static {v4, v5, v8, v3, v12}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v4

    invoke-interface {v4}, Landroid/database/Cursor;->moveToNext()Z

    move-result v5

    if-eqz v5, :cond_c

    const-string v5, "PYMT_METHOD"

    invoke-interface {v4, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v4, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    iput-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    const-string v5, "PYMT_ENTITY"

    invoke-interface {v4, v5}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v5

    invoke-interface {v4, v5}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v5

    iput-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v7, "UPI"

    invoke-virtual {v5, v7}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v5

    if-eqz v5, :cond_b

    const-string v5, "UPI_ID"

    invoke-static {v4, v5, v6}, Lp91;->q(Landroid/database/Cursor;Ljava/lang/String;[B)[B

    move-result-object v5

    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v5}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    goto :goto_6

    :cond_b
    iget-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v7, "WALLET"

    invoke-virtual {v5, v7}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v5

    if-eqz v5, :cond_c

    const-string v5, "W_USER"

    invoke-static {v4, v5, v6}, Lp91;->q(Landroid/database/Cursor;Ljava/lang/String;[B)[B

    move-result-object v5

    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v5}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->h1:Ljava/lang/String;

    :cond_c
    :goto_6
    invoke-interface {v4}, Landroid/database/Cursor;->close()V

    invoke-virtual {v3}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v0}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    :cond_d
    sget-boolean v0, Ljg;->h:Z

    if-nez v0, :cond_e

    iget-boolean v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M1:Z

    if-nez v0, :cond_e

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M1:Z

    new-instance v0, Lyz0;

    invoke-direct {v0}, Lyz0;-><init>()V

    :try_start_4
    const-string v2, "Quota"

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    invoke-virtual {v0, v3, v2}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v2, "Payment method"

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    invoke-virtual {v0, v3, v2}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v2, "Bank"

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0, v3, v2}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    iget-object v1, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v2, "RC service connected"

    invoke-virtual {v1, v0, v2}, Lkf1;->l(Lyz0;Ljava/lang/String;)V
    :try_end_4
    .catch Lxz0; {:try_start_4 .. :try_end_4} :catch_4

    :catch_4
    :cond_e
    return-void
.end method

.method public final k()V
    .locals 4

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->f1:Z

    if-eqz v0, :cond_5

    :try_start_0
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    const-string v1, "@ok"

    invoke-virtual {v0, v1}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    const-string v1, ""

    const/4 v2, 0x1

    if-eqz v0, :cond_0

    :try_start_1
    sput-boolean v2, Ljg;->j:Z

    const-string v0, "com.google.android.apps.nbu.paisa.user"

    goto :goto_1

    :cond_0
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    const-string v3, "@pt"

    invoke-virtual {v0, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    sput-boolean v2, Ljg;->k:Z

    const-string v0, "net.one97.paytm"

    goto :goto_1

    :cond_1
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    const-string v3, "@iPayUpi"

    invoke-virtual {v0, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    const-string v0, "in.org.npci.upiapp"

    goto :goto_1

    :cond_2
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    const-string v3, "@ybl"

    invoke-virtual {v0, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_4

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    const-string v3, "@ibl"

    invoke-virtual {v0, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_4

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    const-string v3, "@axl"

    invoke-virtual {v0, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_3

    goto :goto_0

    :cond_3
    move-object v0, v1

    goto :goto_1

    :cond_4
    :goto_0
    sput-boolean v2, Ljg;->i:Z

    const-string v0, "com.phonepe.app"

    :goto_1
    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_5

    invoke-virtual {p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v1

    invoke-virtual {v1, v0}, Landroid/content/pm/PackageManager;->getLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;

    move-result-object v0

    if-eqz v0, :cond_5

    invoke-virtual {p0, v0}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    :catch_0
    :cond_5
    return-void
.end method

.method public final l(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 30

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    const-string v3, "tv_add_psgn_detail"

    invoke-virtual {v0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v4, "cris.org.in.prs.ima:id/passenger_name"

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    const-string v5, "cris.org.in.prs.ima:id/passenger_age"

    invoke-virtual {v1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    const-string v6, "cris.org.in.prs.ima:id/is_childBerth_req"

    invoke-virtual {v1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v7, "cris.org.in.prs.ima:id/is_bad_berth"

    invoke-virtual {v1, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v7

    const-string v8, "cris.org.in.prs.ima:id/berth_preference_rl"

    invoke-virtual {v1, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v8

    const-string v9, "cris.org.in.prs.ima:id/special_concession_rl"

    invoke-virtual {v1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v9

    const-string v10, "cris.org.in.prs.ima:id/spc_card_number"

    invoke-virtual {v1, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v10

    const-string v11, "cris.org.in.prs.ima:id/tv_conc_dob"

    invoke-virtual {v1, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v11

    const-string v12, "cris.org.in.prs.ima:id/tv_conc_cardvalidity"

    invoke-virtual {v1, v12}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v12

    const-string v13, "cris.org.in.prs.ima:id/tv_done_psgn_detail"

    invoke-virtual {v1, v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    new-instance v14, Ljava/util/HashMap;

    invoke-direct {v14}, Ljava/util/HashMap;-><init>()V

    const-string v15, "tv_male"

    move-object/from16 v16, v9

    const-string v9, "M"

    invoke-virtual {v14, v9, v15}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v15, "tv_female"

    move-object/from16 v17, v9

    const-string v9, "F"

    invoke-virtual {v14, v9, v15}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v15, "tv_transgender"

    move-object/from16 v18, v13

    const-string v13, "T"

    invoke-virtual {v14, v13, v15}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v15, "You have availed senior citizen concession"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v19

    move-object/from16 v20, v13

    const-string v13, "Thank you for forgoing"

    invoke-virtual {v1, v13}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v13

    if-eqz v2, :cond_0

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    :cond_0
    const-string v15, "cris.org.in.prs.ima:id/"

    move-object/from16 v21, v13

    const-string v13, "-"

    move-object/from16 v22, v9

    iget-object v9, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    move-object/from16 v23, v9

    const-string v9, "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE"

    if-eqz v3, :cond_2

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v27

    if-lez v27, :cond_2

    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    move-object/from16 v28, v6

    iget v6, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->q0:I

    if-lt v2, v6, :cond_1

    goto :goto_1

    :cond_1
    :goto_0
    const/16 v2, 0x8

    goto :goto_2

    :cond_2
    move-object/from16 v28, v6

    :goto_1
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    if-eqz v2, :cond_20

    goto :goto_0

    :goto_2
    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v2, 0x0

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    iget-boolean v6, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    if-nez v6, :cond_3

    invoke-interface {v3, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v2, 0x10

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v1, 0x1

    iput-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    return-void

    :cond_3
    const/16 v2, 0x9

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    const-string v3, "HP"

    const-string v6, ""

    if-nez v2, :cond_b

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_9

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    move-object/from16 v29, v8

    iget-object v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    move-object/from16 v17, v8

    iget v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v8, v17, v8

    iget-object v8, v8, Ldo1;->a:Ljava/lang/String;

    invoke-virtual {v2, v9, v8}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v4, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v8, 0x200000

    invoke-virtual {v4, v8, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    move-object/from16 v17, v8

    iget v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v8, v17, v8

    iget v8, v8, Ldo1;->b:I

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v9, v4}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v5, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v8, 0x200000

    invoke-virtual {v4, v8, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v2, v2, v4

    iget-object v2, v2, Ldo1;->c:Ljava/lang/String;

    invoke-virtual {v14, v2}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4, v15}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v2, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_7

    invoke-interface {v11}, Ljava/util/List;->size()I

    move-result v2

    const-string v4, "/"

    if-lez v2, :cond_4

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v5, v5, v8

    iget-object v5, v5, Ldo1;->k:Ljava/lang/String;

    invoke-virtual {v5, v13, v4}, Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v9, v5}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v11, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v8, 0x200000

    invoke-virtual {v5, v8, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    :cond_4
    invoke-interface {v12}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_5

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v5, v5, v8

    iget-object v5, v5, Ldo1;->l:Ljava/lang/String;

    invoke-virtual {v5, v13, v4}, Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v9, v4}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v12, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v8, 0x200000

    invoke-virtual {v4, v8, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    :cond_5
    invoke-interface {v10}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_6

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v4, v4, v5

    iget-object v4, v4, Ldo1;->i:Ljava/lang/String;

    invoke-virtual {v2, v9, v4}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v10, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v8, 0x200000

    invoke-virtual {v4, v8, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    :cond_6
    const-string v2, "cris.org.in.prs.ima:id/et_sp_cardnumber"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v4

    if-lez v4, :cond_7

    new-instance v4, Landroid/os/Bundle;

    invoke-direct {v4}, Landroid/os/Bundle;-><init>()V

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v5, v5, v8

    iget-object v5, v5, Ldo1;->i:Ljava/lang/String;

    invoke-virtual {v4, v9, v5}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v8, 0x200000

    invoke-virtual {v2, v8, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    :cond_7
    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v2, v2, v4

    iget-boolean v2, v2, Ldo1;->h:Z

    if-eqz v2, :cond_8

    invoke-interface {v7}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_8

    const/4 v8, 0x0

    invoke-interface {v7, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v2, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_8
    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v2, v2, v4

    iget-object v2, v2, Ldo1;->d:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_a

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v2, v2, v4

    iget v2, v2, Ldo1;->o:I

    const/4 v4, 0x1

    if-ne v2, v4, :cond_9

    goto :goto_3

    :cond_9
    move-object/from16 v2, v28

    goto/16 :goto_4

    :cond_a
    const/4 v4, 0x1

    :goto_3
    iput v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    move-object/from16 v0, v29

    const/4 v8, 0x0

    invoke-interface {v0, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v0, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_b
    const/4 v4, 0x1

    if-ne v2, v4, :cond_9

    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    new-instance v2, Ljava/util/HashMap;

    invoke-direct {v2}, Ljava/util/HashMap;-><init>()V

    const-string v4, "UB"

    const-string v5, "UPPER"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "LB"

    const-string v5, "LOWER"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "MB"

    const-string v5, "MIDDLE"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "SU"

    const-string v5, "SIDE UPPER"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "SL"

    const-string v5, "SIDE LOWER"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "WS"

    const-string v5, "WINDOW"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "CB"

    const-string v5, "CABIN"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "CP"

    const-string v5, "COUPE"

    invoke-virtual {v2, v4, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "NO PREFERENCE"

    invoke-virtual {v2, v6, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v6, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v5, v5, v6

    iget-object v5, v5, Ldo1;->d:Ljava/lang/String;

    invoke-virtual {v2, v5}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_c

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    const/16 v2, 0x10

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_c
    const/16 v2, 0x10

    const/4 v8, 0x0

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_9

    invoke-interface {v4, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :goto_4
    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v4

    if-nez v4, :cond_d

    const-string v2, "is_childBerth_req"

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    :cond_d
    if-eqz v2, :cond_e

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v4

    if-lez v4, :cond_e

    iget-object v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v4, v4, v5

    iget v4, v4, Ldo1;->o:I

    const/4 v5, 0x1

    if-ne v4, v5, :cond_f

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    if-nez v4, :cond_f

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    return-void

    :cond_e
    const/4 v5, 0x1

    :cond_f
    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    const-string v4, "OK"

    if-ne v2, v5, :cond_11

    const-string v2, "Full fare will be charged"

    move-object/from16 v6, p2

    invoke-virtual {v6, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-static {v5, v0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v0, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_10
    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    goto :goto_5

    :cond_11
    move-object/from16 v6, p2

    :goto_5
    const-string v2, "cris.org.in.prs.ima:id/srctzn_concession_rl"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v5

    if-nez v5, :cond_12

    const-string v2, "srctzn_concession_rl"

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    :cond_12
    if-eqz v2, :cond_13

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_13

    iget v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    if-nez v5, :cond_13

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v5, 0x1

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    return-void

    :cond_13
    const/4 v5, 0x1

    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    if-ne v2, v5, :cond_18

    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    if-nez v2, :cond_17

    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    if-nez v2, :cond_14

    new-instance v2, Landroid/util/SparseArray;

    invoke-direct {v2}, Landroid/util/SparseArray;-><init>()V

    const-string v3, "Avail Concession"

    const/4 v8, 0x0

    invoke-virtual {v2, v8, v3}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const-string v3, "Forgo 50% Concession"

    invoke-virtual {v2, v5, v3}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const-string v3, "Forgo Full Concession"

    const/4 v4, 0x2

    invoke-virtual {v2, v4, v3}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v3, v3, v4

    iget v3, v3, Ldo1;->e:I

    invoke-virtual {v2, v3}, Landroid/util/SparseArray;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const/4 v8, 0x0

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v5, 0x1

    iput-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    return-void

    :cond_14
    const-wide/16 v7, 0x1f4

    :try_start_0
    invoke-static {v7, v8}, Ljava/lang/Thread;->sleep(J)V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    invoke-interface/range {v19 .. v19}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_15

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-static {v5, v0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v2, 0x10

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_15
    const/16 v2, 0x10

    invoke-interface/range {v21 .. v21}, Ljava/util/List;->size()I

    move-result v7

    if-lez v7, :cond_16

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-static {v5, v0}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_16
    iput-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    :cond_17
    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    :cond_18
    const-string v2, "cris.org.in.prs.ima:id/food_choice_rl"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->isEmpty()Z

    move-result v4

    if-eqz v4, :cond_19

    const-string v2, "food_choice_rl"

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    :cond_19
    if-eqz v2, :cond_1a

    invoke-interface {v2}, Ljava/util/List;->isEmpty()Z

    move-result v4

    if-nez v4, :cond_1a

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    if-nez v4, :cond_1a

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v5, 0x1

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    return-void

    :cond_1a
    const/4 v5, 0x1

    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    if-ne v2, v5, :cond_1b

    new-instance v2, Ljava/util/HashMap;

    invoke-direct {v2}, Ljava/util/HashMap;-><init>()V

    const-string v3, "V"

    const-string v4, "VEG"

    invoke-virtual {v2, v3, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v3, "N"

    const-string v4, "NON VEG"

    invoke-virtual {v2, v3, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v3, "J"

    const-string v4, "JAIN MEAL"

    invoke-virtual {v2, v3, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v3, "VEG (DIABETIC)"

    move-object/from16 v4, v22

    invoke-virtual {v2, v4, v3}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v3, "G"

    const-string v4, "NON VEG (DIABETIC)"

    invoke-virtual {v2, v3, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v3, "D"

    const-string v4, "NO FOOD"

    invoke-virtual {v2, v3, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v4, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v3, v3, v4

    iget-object v3, v3, Ldo1;->f:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const/4 v8, 0x0

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    return-void

    :cond_1b
    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1e

    invoke-interface/range {v16 .. v16}, Ljava/util/List;->size()I

    move-result v1

    if-nez v1, :cond_1c

    const-string v1, "special_concession_rl"

    invoke-virtual {v0, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v9

    goto :goto_6

    :cond_1c
    move-object/from16 v9, v16

    :goto_6
    if-eqz v9, :cond_1d

    invoke-interface {v9}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_1d

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    if-nez v1, :cond_1d

    const/4 v8, 0x0

    invoke-interface {v9, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v5, 0x1

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    return-void

    :cond_1d
    const/4 v5, 0x1

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    if-ne v1, v5, :cond_1e

    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    new-instance v1, Landroid/util/SparseArray;

    invoke-direct {v1}, Landroid/util/SparseArray;-><init>()V

    const-string v3, "Divyangjan"

    invoke-virtual {v1, v5, v3}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const-string v3, "Escort"

    invoke-virtual {v1, v2, v3}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    iget v0, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    aget-object v0, v2, v0

    iget-object v0, v0, Ldo1;->j:Ljava/lang/String;

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    invoke-virtual {v1, v0}, Landroid/util/SparseArray;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    invoke-virtual {v6, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const/4 v8, 0x0

    invoke-interface {v0, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    const/16 v4, 0x10

    invoke-virtual {v0, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_1e
    const/4 v8, 0x0

    invoke-interface/range {v18 .. v18}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_3b

    const/16 v2, 0x9

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    const/16 v26, 0x1

    add-int/lit8 v1, v1, 0x1

    iput v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    move-object/from16 v1, v18

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    array-length v2, v2

    if-ne v1, v2, :cond_1f

    const/16 v25, 0x8

    aput-boolean v26, v23, v25

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    :cond_1f
    iget-object v0, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    array-length v0, v0

    if-nez v0, :cond_3b

    const/16 v24, 0x9

    aput-boolean v26, v23, v24

    goto/16 :goto_14

    :cond_20
    move-object/from16 v4, v22

    const-string v2, "cris.org.in.prs.ima:id/tv_infant_psgn_add"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    const-string v3, "cris.org.in.prs.ima:id/passenger_name_child"

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v5, "cris.org.in.prs.ima:id/passenger_age_child"

    invoke-virtual {v1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    new-instance v6, Ljava/util/HashMap;

    invoke-direct {v6}, Ljava/util/HashMap;-><init>()V

    const-string v7, "male_child"

    move-object/from16 v8, v17

    invoke-virtual {v6, v8, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v7, "female_child"

    invoke-virtual {v6, v4, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "transgender_child"

    move-object/from16 v7, v20

    invoke-virtual {v6, v7, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v4, "cris.org.in.prs.ima:id/rl_save_child"

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v7

    if-lez v7, :cond_22

    iget v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iget v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->r0:I

    if-lt v7, v8, :cond_21

    goto :goto_8

    :cond_21
    :goto_7
    const/16 v7, 0x8

    goto :goto_9

    :cond_22
    :goto_8
    iget-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    if-eqz v7, :cond_26

    goto :goto_7

    :goto_9
    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    if-nez v7, :cond_23

    const/4 v7, 0x1

    iput-boolean v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    const/4 v8, 0x0

    invoke-interface {v2, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v0, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_23
    const/4 v7, 0x1

    const/4 v8, 0x0

    const/16 v2, 0xa

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    if-nez v10, :cond_25

    iput v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    invoke-interface {v3, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    new-instance v7, Landroid/os/Bundle;

    invoke-direct {v7}, Landroid/os/Bundle;-><init>()V

    iget-object v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    iget v10, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    aget-object v8, v8, v10

    iget-object v8, v8, Lrl;->a:Ljava/lang/String;

    invoke-virtual {v7, v9, v8}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/high16 v8, 0x200000

    invoke-virtual {v3, v8, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    iget v7, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    aget-object v3, v3, v7

    iget-object v3, v3, Lrl;->c:Ljava/lang/String;

    invoke-virtual {v6, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6, v15}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const/4 v8, 0x0

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v3, 0x10

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    invoke-interface {v5, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_24
    const/4 v1, 0x2

    goto :goto_a

    :cond_25
    move v5, v7

    if-ne v10, v5, :cond_24

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    aget-object v2, v2, v3

    iget-object v2, v2, Lrl;->b:Ljava/lang/String;

    invoke-static {v2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v2

    const-string v3, "Below 1 Year"

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const/4 v8, 0x0

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v1, 0x2

    iput v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    return-void

    :goto_a
    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_3b

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    if-ne v3, v1, :cond_3b

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v8, 0x0

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    iput v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    const/16 v26, 0x1

    add-int/lit8 v1, v1, 0x1

    iput v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    invoke-interface {v4, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iget-object v0, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    array-length v0, v0

    if-ne v1, v0, :cond_3b

    const/16 v24, 0x9

    aput-boolean v26, v23, v24

    return-void

    :cond_26
    const-string v2, "cris.org.in.prs.ima:id/insurance_opt"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    const-string v3, "cris.org.in.prs.ima:id/no_insurance_opt"

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v4, "cris.org.in.prs.ima:id/preferred_coach"

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    const-string v5, "cris.org.in.prs.ima:id/auto_upgradation"

    invoke-virtual {v1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    const-string v6, "cris.org.in.prs.ima:id/book_on_cnf_only"

    invoke-virtual {v1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v6

    const-string v7, "cris.org.in.prs.ima:id/tv_reservation_choice"

    invoke-virtual {v1, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v7

    new-instance v8, Landroid/util/SparseArray;

    invoke-direct {v8}, Landroid/util/SparseArray;-><init>()V

    const-string v10, "None"

    const/4 v11, 0x0

    invoke-virtual {v8, v11, v10}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const-string v10, "Book, only if all berths are allocated in the same coach"

    const/4 v11, 0x1

    invoke-virtual {v8, v11, v10}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const-string v10, "Book, only if atleast 1 lower berth is allocated"

    const/4 v11, 0x2

    invoke-virtual {v8, v11, v10}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const/4 v10, 0x3

    const-string v11, "Book, only if 2 lower berths are allocated"

    invoke-virtual {v8, v10, v11}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    const-string v10, "cris.org.in.prs.ima:id/journey_detail"

    invoke-virtual {v1, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v10

    const-string v11, "cris.org.in.prs.ima:id/et_mobile_number"

    invoke-virtual {v1, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v11

    const-string v12, "cris.org.in.prs.ima:id/tv_boarding_station_select"

    invoke-virtual {v1, v12}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v12

    const-string v14, "cris.org.in.prs.ima:id/food_beverages_chcekbox"

    invoke-virtual {v1, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v14

    const-string v15, "cris.org.in.prs.ima:id/et_address1"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v15, "cris.org.in.prs.ima:id/et_address2"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v15, "cris.org.in.prs.ima:id/et_address3"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v15, "cris.org.in.prs.ima:id/et_psgn_pincode"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v15, "cris.org.in.prs.ima:id/et_city_town"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v15, "cris.org.in.prs.ima:id/et_state"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    const-string v15, "cris.org.in.prs.ima:id/et_post_office"

    invoke-virtual {v1, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    invoke-interface {v10}, Ljava/util/List;->size()I

    move-result v15

    if-gtz v15, :cond_27

    iget-boolean v15, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E0:Z

    if-eqz v15, :cond_3b

    :cond_27
    const/16 v15, 0x8

    iput v15, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget-boolean v15, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E0:Z

    if-nez v15, :cond_38

    iget v15, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->r1:I

    if-nez v15, :cond_32

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v15

    if-lez v15, :cond_29

    iget v15, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->h0:I

    move-object/from16 p2, v10

    const/4 v10, 0x1

    if-ne v15, v10, :cond_28

    const/4 v10, 0x0

    invoke-interface {v2, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v15, 0x10

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto :goto_b

    :cond_28
    const/4 v10, 0x0

    const/16 v15, 0x10

    invoke-interface {v3, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto :goto_b

    :cond_29
    move-object/from16 p2, v10

    :goto_b
    const-string v2, "Pay through BHIM"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_2a

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->m0:I

    const/4 v10, 0x1

    if-eq v3, v10, :cond_2c

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    if-eqz v3, :cond_2a

    const-string v10, "UPI"

    invoke-virtual {v3, v10}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v3

    if-nez v3, :cond_2b

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v10, "APP"

    invoke-virtual {v3, v10}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v3

    if-eqz v3, :cond_2a

    goto :goto_c

    :cond_2a
    const/4 v10, 0x0

    goto :goto_d

    :cond_2b
    :goto_c
    iget-boolean v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->O1:Z

    if-nez v3, :cond_2a

    :cond_2c
    const/4 v10, 0x0

    invoke-interface {v2, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-virtual {v2, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    const/16 v15, 0x10

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :goto_d
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->l0:Z

    if-nez v2, :cond_31

    const/4 v2, 0x1

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->l0:Z

    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->j0:Z

    if-eqz v2, :cond_2d

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_2d

    invoke-interface {v5, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v15, 0x10

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto :goto_e

    :cond_2d
    const/16 v15, 0x10

    :goto_e
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->i0:Z

    if-eqz v2, :cond_2e

    invoke-interface {v14}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_2e

    invoke-interface {v14, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_2e
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->k0:Z

    if-eqz v2, :cond_2f

    invoke-interface {v6}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_2f

    invoke-interface {v6, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_2f
    iget-boolean v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->o0:Z

    if-eqz v2, :cond_30

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_30

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->p0:Ljava/lang/String;

    invoke-virtual {v2, v9, v3}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v10, 0x0

    invoke-interface {v4, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v4, 0x200000

    invoke-virtual {v3, v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    goto :goto_f

    :cond_30
    const/4 v10, 0x0

    :cond_31
    :goto_f
    invoke-interface {v7}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_33

    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->n0:I

    if-eqz v2, :cond_33

    invoke-interface {v7, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v2, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const-string v2, "cris.org.in.prs.ima:id/regervation_choice_ll"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    iget v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->n0:I

    invoke-virtual {v8, v3}, Landroid/util/SparseArray;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    invoke-interface {v2, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_33

    invoke-interface {v2, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v2, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto :goto_10

    :cond_32
    move-object/from16 p2, v10

    :cond_33
    :goto_10
    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d0:Ljava/lang/String;

    if-eqz v2, :cond_36

    invoke-virtual {v2, v13}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v2

    array-length v2, v2

    const/4 v5, 0x1

    if-le v2, v5, :cond_36

    iget v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->r1:I

    if-nez v2, :cond_34

    const/4 v8, 0x0

    invoke-interface {v12, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->r1:I

    return-void

    :cond_34
    const/4 v8, 0x0

    if-ne v2, v5, :cond_36

    const-string v2, "cris.org.in.prs.ima:id/boarding_stn"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d0:Ljava/lang/String;

    const-string v3, " - "

    invoke-virtual {v2, v3}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    aget-object v4, v2, v8

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "  "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/16 v26, 0x1

    aget-object v2, v2, v26

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_35

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_35
    const/4 v2, 0x2

    iput v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->r1:I

    return-void

    :cond_36
    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->g0:Ljava/lang/String;

    invoke-virtual {v2}, Ljava/lang/String;->isEmpty()Z

    move-result v2

    if-nez v2, :cond_37

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v3, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->g0:Ljava/lang/String;

    invoke-virtual {v2, v9, v3}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    const/4 v8, 0x0

    invoke-interface {v11, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/high16 v4, 0x200000

    invoke-virtual {v3, v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    goto :goto_11

    :cond_37
    const/4 v8, 0x0

    :goto_11
    const-string v2, "cris.org.in.prs.ima:id/ticket_sent_level"

    invoke-virtual {v1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_39

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, "and "

    invoke-virtual {v1, v2}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v1

    const/16 v26, 0x1

    aget-object v1, v1, v26

    const-string v2, "\\*\\*\\*\\*"

    invoke-virtual {v1, v2}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v1

    aget-object v1, v1, v26

    invoke-virtual {v1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    goto :goto_12

    :cond_38
    move-object/from16 p2, v10

    :cond_39
    :goto_12
    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->e0:[Ldo1;

    array-length v2, v2

    if-ne v1, v2, :cond_3b

    iget v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iget-object v2, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->f0:[Lrl;

    array-length v2, v2

    if-ne v1, v2, :cond_3b

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D0:Z

    if-nez v1, :cond_3b

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    if-nez v1, :cond_3a

    move-object/from16 v1, p2

    const/4 v8, 0x0

    invoke-interface {v1, v8}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    const/16 v4, 0x10

    invoke-virtual {v1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v5, 0x1

    iput-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    iget-object v1, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v2, "RC passenger submit"

    invoke-virtual {v1, v2}, Lkf1;->m(Ljava/lang/String;)V

    goto :goto_13

    :cond_3a
    const/4 v5, 0x1

    const/4 v8, 0x0

    :goto_13
    iput-boolean v5, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->D0:Z

    iput-boolean v8, v0, Lcom/tatkal/train/quick/MyAccessibilityService;->E0:Z

    const/4 v0, 0x7

    aput-boolean v5, v23, v0

    :cond_3b
    :goto_14
    return-void
.end method

.method public final m(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 14

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->O1:Z

    if-eqz v0, :cond_0

    goto/16 :goto_7

    :cond_0
    const-string v0, "SELECT A PAYMENT PROVIDER"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "SELECT A PAYMENT METHOD"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->c(Landroid/view/accessibility/AccessibilityNodeInfo;)Z

    move-result v2

    if-eqz v2, :cond_1

    goto/16 :goto_7

    :cond_1
    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    const-string v2, "BHIM"

    const-string v3, "EWALLET"

    const-string v4, "OTHERS"

    const/4 v5, -0x1

    const/4 v6, 0x2

    const/16 v7, 0x10

    const-string v8, "cris.org.in.prs.ima:id/"

    iget-object v9, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    const-string v10, ""

    const/4 v11, 0x0

    const/4 v12, 0x1

    if-eqz v0, :cond_2

    invoke-interface {v1}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_6

    :cond_2
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t:Z

    if-nez v0, :cond_6

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    if-eqz v0, :cond_6

    invoke-virtual {v0}, Ljava/lang/String;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_6

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v1, "RC payment screen"

    invoke-virtual {v0, v1}, Lkf1;->m(Ljava/lang/String;)V

    const/16 v0, 0xc

    aput-boolean v12, v9, v0

    const/16 v0, 0xe

    iput v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iput-boolean v11, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->M:Z

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-virtual {v0}, Ljava/lang/String;->hashCode()I

    move-result v1

    sparse-switch v1, :sswitch_data_0

    goto :goto_0

    :sswitch_0
    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_3

    goto :goto_0

    :cond_3
    move v5, v6

    goto :goto_0

    :sswitch_1
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_4

    goto :goto_0

    :cond_4
    move v5, v12

    goto :goto_0

    :sswitch_2
    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_5

    goto :goto_0

    :cond_5
    move v5, v11

    :goto_0
    packed-switch v5, :pswitch_data_0

    goto :goto_1

    :pswitch_0
    const-string v10, "rl_bhim_upi_head"

    goto :goto_1

    :pswitch_1
    const-string v10, "rv_select_wallet"

    goto :goto_1

    :pswitch_2
    const-string v10, "other_payment"

    :goto_1
    iput-boolean v12, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t:Z

    invoke-virtual {v8, v10}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0, v11}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_6
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t:Z

    const-string v1, "MOBIKWIK_EWALLET"

    if-eqz v0, :cond_7

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u:Z

    if-eqz v0, :cond_8

    :cond_7
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    if-eqz v0, :cond_1b

    :cond_8
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    if-eqz v0, :cond_1b

    invoke-virtual {v0, v10}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1b

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-virtual {v0}, Ljava/lang/String;->hashCode()I

    move-result v13

    sparse-switch v13, :sswitch_data_1

    :goto_2
    move v0, v5

    goto :goto_3

    :sswitch_3
    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_9

    goto :goto_2

    :cond_9
    move v0, v6

    goto :goto_3

    :sswitch_4
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_a

    goto :goto_2

    :cond_a
    move v0, v12

    goto :goto_3

    :sswitch_5
    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_b

    goto :goto_2

    :cond_b
    move v0, v11

    :goto_3
    const/4 v2, 0x4

    const/4 v3, 0x3

    const/4 v4, 0x5

    packed-switch v0, :pswitch_data_1

    goto/16 :goto_5

    :pswitch_3
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->T:Ljava/lang/String;

    goto/16 :goto_6

    :pswitch_4
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-virtual {v0}, Ljava/lang/String;->hashCode()I

    move-result v13

    sparse-switch v13, :sswitch_data_2

    goto :goto_4

    :sswitch_6
    const-string v13, "PAYTM_EWALLET"

    invoke-virtual {v0, v13}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_c

    goto :goto_4

    :cond_c
    move v5, v4

    goto :goto_4

    :sswitch_7
    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_d

    goto :goto_4

    :cond_d
    move v5, v2

    goto :goto_4

    :sswitch_8
    const-string v13, "AIRTEL_EWALLET"

    invoke-virtual {v0, v13}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_e

    goto :goto_4

    :cond_e
    move v5, v3

    goto :goto_4

    :sswitch_9
    const-string v13, "OLAMONEY_EWALLET"

    invoke-virtual {v0, v13}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_f

    goto :goto_4

    :cond_f
    move v5, v6

    goto :goto_4

    :sswitch_a
    const-string v13, "IRCTC_EWALLET"

    invoke-virtual {v0, v13}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_10

    goto :goto_4

    :cond_10
    move v5, v12

    goto :goto_4

    :sswitch_b
    const-string v13, "JIOMONEY_EWALLET"

    invoke-virtual {v0, v13}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_11

    goto :goto_4

    :cond_11
    move v5, v11

    :goto_4
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->R:[Ljava/lang/String;

    packed-switch v5, :pswitch_data_2

    goto/16 :goto_5

    :pswitch_5
    aget-object v0, v0, v6

    goto/16 :goto_6

    :pswitch_6
    aget-object v0, v0, v12

    goto/16 :goto_6

    :pswitch_7
    aget-object v0, v0, v4

    goto/16 :goto_6

    :pswitch_8
    aget-object v0, v0, v3

    goto/16 :goto_6

    :pswitch_9
    aget-object v0, v0, v11

    goto/16 :goto_6

    :pswitch_a
    aget-object v0, v0, v2

    goto/16 :goto_6

    :pswitch_b
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v5, "IPAY"

    invoke-virtual {v0, v5}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    iget-object v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S:[Ljava/lang/String;

    if-eqz v0, :cond_12

    aget-object v0, v5, v11

    goto/16 :goto_6

    :cond_12
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v13, "PAYTM"

    invoke-virtual {v0, v13}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_13

    aget-object v0, v5, v12

    goto :goto_6

    :cond_13
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v13, "MOBIKWIK"

    invoke-virtual {v0, v13}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_14

    aget-object v0, v5, v6

    goto :goto_6

    :cond_14
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v6, "PAYU"

    invoke-virtual {v0, v6}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_15

    aget-object v0, v5, v3

    goto :goto_6

    :cond_15
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v3, "RAZORPAY_"

    invoke-virtual {v0, v3}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_16

    aget-object v0, v5, v2

    goto :goto_6

    :cond_16
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v2, "PHONEPE"

    invoke-virtual {v0, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_17

    aget-object v0, v5, v4

    goto :goto_6

    :cond_17
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v2, "ICICI"

    invoke-virtual {v0, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_18

    const/4 v0, 0x6

    aget-object v0, v5, v0

    goto :goto_6

    :cond_18
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v2, "HDFC"

    invoke-virtual {v0, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_19

    const/4 v0, 0x7

    aget-object v0, v5, v0

    goto :goto_6

    :cond_19
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    const-string v2, "AIRPAY"

    invoke-virtual {v0, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1a

    const/16 v0, 0x8

    aget-object v0, v5, v0

    goto :goto_6

    :cond_1a
    :goto_5
    move-object v0, v10

    :goto_6
    iget-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u:Z

    if-nez v2, :cond_1b

    iput-boolean v12, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u:Z

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2, v8}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v11}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1b

    goto :goto_7

    :cond_1b
    const-string v0, "cris.org.in.prs.ima:id/proceed_to_payment"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    iget-boolean p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->v:Z

    if-nez p1, :cond_1c

    iget-boolean p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u:Z

    if-eqz p1, :cond_1c

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    if-eqz p1, :cond_1c

    invoke-virtual {p1, v10}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_1c

    iput-boolean v12, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->v:Z

    iput-boolean v12, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    const/16 p1, 0xd

    aput-boolean v12, v9, p1

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_1c

    iput-boolean v11, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u:Z

    iput-boolean v11, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t:Z

    :cond_1c
    :goto_7
    return-void

    :sswitch_data_0
    .sparse-switch
        -0x746fa89d -> :sswitch_2
        -0x25c4bf82 -> :sswitch_1
        0x1f17ea -> :sswitch_0
    .end sparse-switch

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch

    :sswitch_data_1
    .sparse-switch
        -0x746fa89d -> :sswitch_5
        -0x25c4bf82 -> :sswitch_4
        0x1f17ea -> :sswitch_3
    .end sparse-switch

    :pswitch_data_1
    .packed-switch 0x0
        :pswitch_b
        :pswitch_4
        :pswitch_3
    .end packed-switch

    :sswitch_data_2
    .sparse-switch
        -0x3388e911 -> :sswitch_b
        -0x31145198 -> :sswitch_a
        -0x183e5ba5 -> :sswitch_9
        -0xe79fb30 -> :sswitch_8
        0x2cad3cb6 -> :sswitch_7
        0x68089340 -> :sswitch_6
    .end sparse-switch

    :pswitch_data_2
    .packed-switch 0x0
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
    .end packed-switch
.end method

.method public final n(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 7

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->o(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    if-ge v2, v0, :cond_1

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {p0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->o(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v4

    if-lez v4, :cond_0

    move v5, v1

    :goto_1
    if-ge v5, v4, :cond_0

    invoke-virtual {v3, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {p0, v6}, Lcom/tatkal/train/quick/MyAccessibilityService;->n(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_0
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public final o(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 10

    const-string v0, "Complete Payment to"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "Decline Payment"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const-string v2, "Pay Securely"

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    iget-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->E1:Z

    const-string v3, "android.widget.TextView"

    const-string v4, "PAY"

    const/16 v5, 0x10

    const/4 v6, 0x1

    const/4 v7, 0x0

    if-nez v2, :cond_1

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_1

    invoke-interface {v0, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result p1

    move v1, v7

    :goto_0
    if-ge v1, p1, :cond_4

    invoke-interface {v0, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-virtual {v2, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v8

    if-eqz v8, :cond_0

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v8

    invoke-interface {v8}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v8

    sget-object v9, Ljava/util/Locale;->ROOT:Ljava/util/Locale;

    invoke-virtual {v8, v9}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v8, v4}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v8

    if-eqz v8, :cond_0

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v8

    invoke-virtual {v8, v3}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v8

    if-eqz v8, :cond_0

    invoke-virtual {v2, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->E1:Z

    return-void

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_3

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->E1:Z

    if-nez v0, :cond_3

    invoke-interface {v1, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result p1

    move v0, v7

    :goto_1
    if-ge v0, p1, :cond_4

    invoke-interface {v1, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-virtual {v2, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v8

    if-eqz v8, :cond_2

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v8

    invoke-interface {v8}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v8

    sget-object v9, Ljava/util/Locale;->ROOT:Ljava/util/Locale;

    invoke-virtual {v8, v9}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v8, v4}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v8

    if-eqz v8, :cond_2

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v8

    invoke-virtual {v8, v3}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v8

    if-eqz v8, :cond_2

    invoke-virtual {v2, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->E1:Z

    return-void

    :cond_2
    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    :cond_3
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->F1:Z

    if-nez v0, :cond_4

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_4

    sget-boolean v0, Ljg;->m:Z

    if-eqz v0, :cond_4

    invoke-interface {p1, v7}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->F1:Z

    :cond_4
    return-void
.end method

.method public final onAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V
    .locals 5

    const-string v0, "\n\n\n\n"

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/Calendar;->getTimeInMillis()J

    move-result-wide v1

    iget-wide v3, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->P1:J

    sub-long v3, v1, v3

    iput-wide v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->P1:J

    const-wide/16 v1, 0x64

    cmp-long v1, v3, v1

    if-gez v1, :cond_0

    goto/16 :goto_3

    :cond_0
    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityEvent;->getPackageName()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, "org.cris.aikyam"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    const/4 v3, 0x1

    const/4 v4, 0x0

    if-eqz v2, :cond_3

    invoke-virtual {p0}, Landroid/accessibilityservice/AccessibilityService;->getRootInActiveWindow()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    const-string v1, ""

    sput-object v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    invoke-static {p1, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    sget-object p1, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    sget-object v1, Lmc1;->c:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_9

    sget-object p1, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    sput-object p1, Lmc1;->c:Ljava/lang/String;

    sget-object p1, Landroid/os/Environment;->DIRECTORY_DOCUMENTS:Ljava/lang/String;

    invoke-virtual {p0, p1}, Landroid/content/Context;->getExternalFilesDir(Ljava/lang/String;)Ljava/io/File;

    move-result-object p0

    if-eqz p0, :cond_1

    invoke-virtual {p0}, Ljava/io/File;->exists()Z

    move-result p1

    if-nez p1, :cond_1

    invoke-virtual {p0}, Ljava/io/File;->mkdirs()Z

    :cond_1
    new-instance p1, Ljava/io/File;

    const-string v1, "railone.txt"

    invoke-direct {p1, p0, v1}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    :try_start_0
    new-instance p0, Ljava/io/FileOutputStream;

    sget v1, Lmc1;->d:I

    if-eq v1, v3, :cond_2

    move v4, v3

    :cond_2
    invoke-direct {p0, p1, v4}, Ljava/io/FileOutputStream;-><init>(Ljava/io/File;Z)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    :try_start_1
    sget-object p1, Lmc1;->c:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1}, Ljava/lang/String;->getBytes()[B

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/FileOutputStream;->write([B)V

    invoke-virtual {p0}, Ljava/io/OutputStream;->flush()V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :try_start_2
    invoke-virtual {p0}, Ljava/io/FileOutputStream;->close()V
    :try_end_2
    .catch Ljava/io/IOException; {:try_start_2 .. :try_end_2} :catch_0

    goto :goto_1

    :catchall_0
    move-exception p1

    :try_start_3
    invoke-virtual {p0}, Ljava/io/FileOutputStream;->close()V
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    goto :goto_0

    :catchall_1
    move-exception p0

    :try_start_4
    invoke-virtual {p1, p0}, Ljava/lang/Throwable;->addSuppressed(Ljava/lang/Throwable;)V

    :goto_0
    throw p1
    :try_end_4
    .catch Ljava/io/IOException; {:try_start_4 .. :try_end_4} :catch_0

    :catch_0
    :goto_1
    sget p0, Lmc1;->d:I

    add-int/2addr p0, v3

    sput p0, Lmc1;->d:I

    return-void

    :cond_3
    const-string v0, "com.tatkal.train.ticket"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_8

    const-string v0, "com.tatkal.train.quick"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    goto :goto_2

    :cond_4
    :try_start_5
    invoke-virtual {p0}, Landroid/accessibilityservice/AccessibilityService;->getRootInActiveWindow()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    const-string v1, "cris.org.in.prs.ima:id/my_journey_ll"

    invoke-virtual {v0, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    if-eqz v0, :cond_5

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_5

    sput v4, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I
    :try_end_5
    .catch Ljava/lang/Exception; {:try_start_5 .. :try_end_5} :catch_1

    :catch_1
    :cond_5
    sget v0, Lcom/tatkal/train/quick/MyAccessibilityService;->d2:I

    if-eq v0, v3, :cond_9

    const/4 v1, 0x2

    if-ne v0, v1, :cond_6

    goto :goto_3

    :cond_6
    invoke-virtual {p0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    const-string v1, "TICKET_INFO"

    invoke-virtual {v0, v1, v4}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v0

    const-string v1, "TICKETS_LEFT"

    invoke-interface {v0, v1, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v0

    if-gtz v0, :cond_7

    sget-object v0, Ljg;->y:Ljava/lang/String;

    const-string v1, "FREE_USER"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_9

    sget-object v0, Ljg;->y:Ljava/lang/String;

    const-string v1, "COMP_USER"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_9

    :cond_7
    :try_start_6
    invoke-virtual {p0}, Landroid/accessibilityservice/AccessibilityService;->getRootInActiveWindow()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    if-eqz v0, :cond_9

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityRecord;->getSource()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p0}, Landroid/accessibilityservice/AccessibilityService;->getRootInActiveWindow()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {p0, p1, v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->b(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    :try_end_6
    .catch Ljava/lang/Exception; {:try_start_6 .. :try_end_6} :catch_2

    goto :goto_3

    :cond_8
    :goto_2
    sget-object p1, Lcom/tatkal/train/quick/TabActivity2;->t3:Ljava/lang/String;

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Landroid/content/Context;->getContentResolver()Landroid/content/ContentResolver;

    move-result-object p0

    const-string v0, "default_input_method"

    invoke-static {p0, v0}, Landroid/provider/Settings$Secure;->getString(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    if-eqz p0, :cond_9

    invoke-virtual {p0, p1}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    :catch_2
    :cond_9
    :goto_3
    return-void
.end method

.method public final onDestroy()V
    .locals 1

    invoke-super {p0}, Landroid/app/Service;->onDestroy()V

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->b:Z

    if-eqz v0, :cond_0

    :try_start_0
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->X1:Lph1;

    invoke-virtual {p0, v0}, Landroid/content/Context;->unbindService(Landroid/content/ServiceConnection;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->b:Z

    :cond_0
    return-void
.end method

.method public final onInterrupt()V
    .locals 0

    return-void
.end method

.method public final onServiceConnected()V
    .locals 13

    invoke-super {p0}, Landroid/accessibilityservice/AccessibilityService;->onServiceConnected()V

    sget-object v0, Lcom/tatkal/train/quick/QuickTatkalApp;->a:Lkf1;

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    sget-boolean v1, Lcom/tatkal/train/quick/TabActivity2;->x3:Z

    if-eqz v1, :cond_0

    const-string v1, "Acc service enabled"

    invoke-virtual {v0, v1}, Lkf1;->m(Ljava/lang/String;)V

    :cond_0
    new-instance v0, Landroid/os/Bundle;

    invoke-direct {v0}, Landroid/os/Bundle;-><init>()V

    const-string v1, "tickets"

    sget v2, Lcom/tatkal/train/quick/SplashActivity;->t:I

    invoke-virtual {v0, v1, v2}, Landroid/os/BaseBundle;->putInt(Ljava/lang/String;I)V

    invoke-static {p0}, Lcom/google/firebase/analytics/FirebaseAnalytics;->getInstance(Landroid/content/Context;)Lcom/google/firebase/analytics/FirebaseAnalytics;

    move-result-object v1

    const-string v2, "book_rc_connect"

    invoke-virtual {v1, v0, v2}, Lcom/google/firebase/analytics/FirebaseAnalytics;->a(Landroid/os/Bundle;Ljava/lang/String;)V

    const-string v0, "ACC_TNC_3"

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v0

    invoke-interface {v0}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v0

    const-string v2, "ACCEPT"

    const/4 v3, 0x1

    invoke-interface {v0, v2, v3}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v0}, Landroid/content/SharedPreferences$Editor;->apply()V

    sput-object p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Z1:Lcom/tatkal/train/quick/MyAccessibilityService;

    new-instance v0, Landroid/content/Intent;

    const-class v2, Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-direct {v0, p0, v2}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    sget-boolean v2, Lcom/tatkal/train/quick/TabActivity2;->u3:Z

    if-nez v2, :cond_1

    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    :cond_1
    :try_start_0
    iget-object v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->X1:Lph1;

    invoke-virtual {p0, v0, v2, v3}, Landroid/content/Context;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;I)Z
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    invoke-virtual {p0}, Landroid/accessibilityservice/AccessibilityService;->getServiceInfo()Landroid/accessibilityservice/AccessibilityServiceInfo;

    move-result-object v0

    const/16 v2, 0x820

    iput v2, v0, Landroid/accessibilityservice/AccessibilityServiceInfo;->eventTypes:I

    const-string v11, "com.tatkal.train.ticket"

    const-string v12, "com.tatkal.train.quick"

    const-string v4, "cris.org.in.prs.ima"

    const-string v5, "com.phonepe.app"

    const-string v6, "com.google.android.apps.nbu.paisa.user"

    const-string v7, "net.one97.paytm"

    const-string v8, "in.org.npci.upiapp"

    const-string v9, "org.cris.aikyam"

    const-string v10, "com.dreamplug.androidapp"

    filled-new-array/range {v4 .. v12}, [Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Landroid/accessibilityservice/AccessibilityServiceInfo;->packageNames:[Ljava/lang/String;

    const/16 v2, 0x10

    iput v2, v0, Landroid/accessibilityservice/AccessibilityServiceInfo;->feedbackType:I

    const-wide/16 v4, 0x64

    iput-wide v4, v0, Landroid/accessibilityservice/AccessibilityServiceInfo;->notificationTimeout:J

    invoke-virtual {p0, v0}, Landroid/accessibilityservice/AccessibilityService;->setServiceInfo(Landroid/accessibilityservice/AccessibilityServiceInfo;)V

    new-instance v0, Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-direct {v0, p0}, Lcom/tatkal/train/quick/AdvancedWebView;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p1:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Landroid/webkit/WebView;->getSettings()Landroid/webkit/WebSettings;

    move-result-object v0

    invoke-virtual {v0, v1}, Landroid/webkit/WebSettings;->setLoadsImagesAutomatically(Z)V

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p1:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Landroid/webkit/WebView;->getSettings()Landroid/webkit/WebSettings;

    move-result-object v0

    invoke-virtual {v0, v3}, Landroid/webkit/WebSettings;->setJavaScriptEnabled(Z)V

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p1:Lcom/tatkal/train/quick/AdvancedWebView;

    new-instance v2, Lrh1;

    invoke-direct {v2, p0}, Lrh1;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;)V

    const-string v4, "Step"

    invoke-virtual {v0, v2, v4}, Landroid/webkit/WebView;->addJavascriptInterface(Ljava/lang/Object;Ljava/lang/String;)V

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p1:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Landroid/webkit/WebView;->getSettings()Landroid/webkit/WebSettings;

    move-result-object v0

    invoke-virtual {v0, v3}, Landroid/webkit/WebSettings;->setDomStorageEnabled(Z)V

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p1:Lcom/tatkal/train/quick/AdvancedWebView;

    new-instance v2, La4;

    const/4 v4, 0x4

    invoke-direct {v2, p0, v4}, La4;-><init>(Ljava/lang/Object;I)V

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/AdvancedWebView;->setWebViewClient(Landroid/webkit/WebViewClient;)V

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->p1:Lcom/tatkal/train/quick/AdvancedWebView;

    const-string v2, "https://www.irctc.co.in/nget/train-search"

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/AdvancedWebView;->loadUrl(Ljava/lang/String;)V

    sput-boolean v3, Lk7;->t:Z

    sget-boolean v0, Lcom/tatkal/train/quick/FormActivity2;->y:Z

    if-eqz v0, :cond_5

    sget-boolean v0, Ljg;->h:Z

    if-nez v0, :cond_2

    invoke-virtual {p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v0

    const-string v2, "cris.org.in.prs.ima"

    invoke-virtual {v0, v2}, Landroid/content/pm/PackageManager;->getLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;

    move-result-object v0

    if-eqz v0, :cond_5

    invoke-virtual {p0, v0}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V

    sput-boolean v1, Lcom/tatkal/train/quick/FormActivity2;->y:Z

    goto/16 :goto_0

    :cond_2
    invoke-static {p0}, Landroid/preference/PreferenceManager;->getDefaultSharedPreferences(Landroid/content/Context;)Landroid/content/SharedPreferences;

    move-result-object v0

    const-string v2, "OPTION"

    invoke-interface {v0, v2, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v0

    new-instance v2, Landroid/content/Intent;

    const-class v4, Lcom/tatkal/train/quick/WebActivity;

    invoke-direct {v2, p0, v4}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    const-string v4, "FORM_NAME"

    sget-object v5, Lcom/tatkal/train/quick/MyAccessibilityService;->a2:Ljava/lang/String;

    invoke-virtual {v2, v4, v5}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    const-string v4, "type"

    const-string v5, "Quick Booking"

    invoke-virtual {v2, v4, v5}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    new-array v4, v3, [Z

    aput-boolean v1, v4, v1

    const/high16 v5, 0x10000000

    if-nez v0, :cond_3

    sget-object v0, Lcom/tatkal/train/quick/TabActivity2;->t3:Ljava/lang/String;

    new-instance v0, Landroid/content/Intent;

    const-class v1, Lcom/tatkal/train/quick/TabActivity2;

    invoke-direct {v0, p0, v1}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    invoke-virtual {v0, v5}, Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;

    const/high16 v1, 0x20000

    invoke-virtual {v0, v1}, Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;

    invoke-virtual {p0, v0}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V

    goto :goto_0

    :cond_3
    const-string v6, "LANG"

    if-ne v0, v3, :cond_4

    const-string v0, "HIN"

    invoke-virtual {v2, v6, v0}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    invoke-virtual {v2, v5}, Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v5, Lyw1;->best_of_luck:I

    invoke-virtual {v0, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    invoke-virtual {p0, v2}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V

    aput-boolean v3, v4, v1

    sput-boolean v3, Lk7;->t:Z

    goto :goto_0

    :cond_4
    new-instance v0, Landroid/app/AlertDialog$Builder;

    invoke-direct {v0, p0}, Landroid/app/AlertDialog$Builder;-><init>(Landroid/content/Context;)V

    const-string v7, "In which language do you prefer to book on IRCTC website?"

    invoke-virtual {v0, v7}, Landroid/app/AlertDialog$Builder;->setMessage(Ljava/lang/CharSequence;)Landroid/app/AlertDialog$Builder;

    const-string v7, "IRCTC Language"

    invoke-virtual {v0, v7}, Landroid/app/AlertDialog$Builder;->setTitle(Ljava/lang/CharSequence;)Landroid/app/AlertDialog$Builder;

    new-instance v7, Lqh1;

    invoke-direct {v7, p0, v2, v4, v1}, Lqh1;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;Landroid/content/Intent;[ZI)V

    const-string v8, "English"

    invoke-virtual {v0, v8, v7}, Landroid/app/AlertDialog$Builder;->setPositiveButton(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;

    new-instance v7, Lqh1;

    invoke-direct {v7, p0, v2, v4, v3}, Lqh1;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;Landroid/content/Intent;[ZI)V

    const-string v8, "\u0939\u093f\u0928\u094d\u0926\u0940"

    invoke-virtual {v0, v8, v7}, Landroid/app/AlertDialog$Builder;->setNegativeButton(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;

    invoke-virtual {v0}, Landroid/app/AlertDialog$Builder;->create()Landroid/app/AlertDialog;

    move-result-object v0

    :try_start_1
    invoke-virtual {v0}, Landroid/app/Dialog;->show()V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    goto :goto_0

    :catch_1
    const-string v0, "ENG"

    invoke-virtual {v2, v6, v0}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    invoke-virtual {v2, v5}, Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v5, Lyw1;->best_of_luck:I

    invoke-virtual {v0, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    invoke-virtual {p0, v2}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V

    aput-boolean v3, v4, v1

    sput-boolean v3, Lk7;->t:Z

    :cond_5
    :goto_0
    return-void
.end method

.method public final p(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 11

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, ".RelativeLayout"

    invoke-virtual {v1, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    const/4 v3, 0x4

    const/16 v4, 0x10

    const/4 v5, 0x3

    if-eqz v1, :cond_0

    iget v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    if-ne v1, v5, :cond_0

    invoke-virtual {p1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v3, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    return-void

    :cond_0
    const/4 v1, 0x0

    move v6, v1

    :goto_0
    if-ge v6, v0, :cond_3

    invoke-virtual {p1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v7

    invoke-virtual {v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v8

    invoke-interface {v8}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v9

    invoke-virtual {v8, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v8

    if-eqz v8, :cond_1

    iget v8, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    if-ne v8, v5, :cond_1

    invoke-virtual {v7, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v3, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    return-void

    :cond_1
    if-lez v9, :cond_2

    move v8, v1

    :goto_1
    if-ge v8, v9, :cond_2

    invoke-virtual {v7, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v10

    invoke-virtual {p0, v10}, Lcom/tatkal/train/quick/MyAccessibilityService;->p(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    add-int/lit8 v8, v8, 0x1

    goto :goto_1

    :cond_2
    add-int/lit8 v6, v6, 0x1

    goto :goto_0

    :cond_3
    return-void
.end method

.method public final q(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 7

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->r(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    if-ge v2, v0, :cond_1

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {p0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->r(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v4

    if-lez v4, :cond_0

    move v5, v1

    :goto_1
    if-ge v5, v4, :cond_0

    invoke-virtual {v3, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {p0, v6}, Lcom/tatkal/train/quick/MyAccessibilityService;->q(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_0
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public final r(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 7

    const-string v0, "Requested by"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "PROCEED TO PAY"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const-string v2, "Total Payable"

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    iget-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->B1:Z

    const/16 v3, 0x10

    const/4 v4, 0x1

    const/4 v5, 0x0

    const-string v6, "PAY"

    if-nez v2, :cond_1

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_1

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_0
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_4

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->B1:Z

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_1
    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_4

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-nez v0, :cond_2

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->C1:Z

    if-nez v0, :cond_2

    invoke-interface {v1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->C1:Z

    return-void

    :cond_2
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->D1:Z

    if-nez v0, :cond_4

    sget-boolean v0, Ljg;->m:Z

    if-eqz v0, :cond_4

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_3
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_4

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v1

    invoke-interface {v1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1, v6}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->D1:Z

    :cond_4
    return-void
.end method

.method public final s(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 13

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->O1:Z

    if-eqz v0, :cond_0

    goto/16 :goto_2

    :cond_0
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->v0:Z

    if-eqz v0, :cond_1e

    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->t0:Ljava/lang/String;

    const-string v1, "OTHERS"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const-string v1, "PROCEED TO PAY"

    const/4 v2, 0x2

    const/16 v3, 0x10

    const/4 v4, 0x1

    const/4 v5, 0x0

    if-eqz v0, :cond_2

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    const-string v6, "AUTOPAY"

    const-string v7, "Autopay"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v6, "EMI"

    const-string v7, "EMI on Cards"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v6, "PAY_LATER"

    const-string v7, "Pay Later"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v6, "CREDIT_CARD"

    const-string v7, "Credit Card"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v6, "DEBIT_CARD"

    const-string v7, "Debit Card"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v6, "INTER_CARD"

    const-string v7, "International Card"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v6, "NETBANKING"

    const-string v7, "NetBanking"

    invoke-virtual {v0, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :try_start_0
    iget v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->l1:I

    if-nez v6, :cond_1

    iget-object v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->l1:I

    goto/16 :goto_2

    :cond_1
    if-ne v6, v4, :cond_1e

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->l1:I
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_2

    goto/16 :goto_2

    :cond_2
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    invoke-virtual {v0}, Ljava/lang/String;->hashCode()I

    move-result v6

    const-string v7, "APP_CRED"

    const-string v8, "APP_PAYTM"

    const-string v9, "APP_PHONEPE"

    const/4 v10, 0x4

    const/4 v11, 0x3

    const/4 v12, -0x1

    sparse-switch v6, :sswitch_data_0

    goto :goto_0

    :sswitch_0
    invoke-virtual {v0, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_3

    goto :goto_0

    :cond_3
    const/4 v12, 0x6

    goto :goto_0

    :sswitch_1
    const-string v6, "UPI_PAYTM"

    invoke-virtual {v0, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_4

    goto :goto_0

    :cond_4
    const/4 v12, 0x5

    goto :goto_0

    :sswitch_2
    const-string v6, "PAYTM_EWALLET"

    invoke-virtual {v0, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_5

    goto :goto_0

    :cond_5
    move v12, v10

    goto :goto_0

    :sswitch_3
    invoke-virtual {v0, v8}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_6

    goto :goto_0

    :cond_6
    move v12, v11

    goto :goto_0

    :sswitch_4
    const-string v6, "MOBIKWIK_EWALLET"

    invoke-virtual {v0, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_7

    goto :goto_0

    :cond_7
    move v12, v2

    goto :goto_0

    :sswitch_5
    invoke-virtual {v0, v9}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_8

    goto :goto_0

    :cond_8
    move v12, v4

    goto :goto_0

    :sswitch_6
    const-string v6, "IRCTC_EWALLET"

    invoke-virtual {v0, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_9

    goto :goto_0

    :cond_9
    move v12, v5

    :goto_0
    const/high16 v0, 0x200000

    const-string v6, "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE"

    packed-switch v12, :pswitch_data_0

    goto/16 :goto_2

    :pswitch_0
    iget-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->m1:Z

    if-nez v2, :cond_1e

    iget-boolean v7, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->n1:Z

    if-nez v7, :cond_a

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_1e

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->n1:Z

    goto/16 :goto_2

    :cond_a
    iget-boolean v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->o1:Z

    if-eqz v1, :cond_b

    if-nez v2, :cond_1e

    :cond_b
    if-eqz v2, :cond_c

    goto/16 :goto_2

    :cond_c
    const-string v1, "EditText"

    invoke-virtual {p0, p1, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->f(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    if-eqz v1, :cond_d

    :try_start_1
    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e1:Ljava/lang/String;

    invoke-virtual {v2, v6, v5}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    invoke-virtual {v1, v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->o1:Z
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    :catch_0
    :cond_d
    invoke-static {p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->h(Landroid/view/accessibility/AccessibilityNodeInfo;)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    if-eqz p1, :cond_1e

    :try_start_2
    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    invoke-virtual {p0}, Lcom/tatkal/train/quick/MyAccessibilityService;->k()V

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->m1:Z
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_2

    goto/16 :goto_2

    :pswitch_1
    iget v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    const-string v7, "Login with different Mobile number"

    const-string v8, "STUDIOS"

    if-nez v1, :cond_10

    const-string v0, "registered with Paytm"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_e

    const-string v0, "TRY DIFFERENT MOBILE NO"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_e

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_e
    iget-object v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->h1:Ljava/lang/String;

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_f

    const-string p1, "MOBILE NUMBER FOUND"

    invoke-static {v8, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    return-void

    :cond_f
    invoke-virtual {p1, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_1e

    const-string v0, "TRYING DIFFERENT NUMBER"

    invoke-static {v8, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    return-void

    :cond_10
    if-ne v1, v4, :cond_11

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v1

    if-le v1, v2, :cond_11

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const-string v4, "LOGIN"

    invoke-virtual {p1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result v4

    if-nez v4, :cond_1e

    const-string v4, "CLICKING LOGIN"

    invoke-static {v8, v4}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :try_start_3
    const-string v4, ""

    sput-object v4, Lcom/tatkal/train/quick/Dashboard;->L:Ljava/lang/String;
    :try_end_3
    .catch Ljava/lang/Exception; {:try_start_3 .. :try_end_3} :catch_1

    :catch_1
    new-instance v4, Landroid/os/Bundle;

    invoke-direct {v4}, Landroid/os/Bundle;-><init>()V

    iget-object v7, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->h1:Ljava/lang/String;

    invoke-virtual {v4, v6, v7}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    invoke-virtual {v1, v0, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    goto/16 :goto_2

    :cond_11
    iget v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    if-ne v1, v2, :cond_13

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v1

    if-le v1, v2, :cond_13

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    const-string v9, "SUBMIT"

    invoke-virtual {p1, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v9

    invoke-virtual {p1, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result p1

    if-nez p1, :cond_1e

    const-string p1, "ON OTP SCREEN"

    invoke-static {v8, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput v11, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    iput-object v1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->O0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-interface {v9, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    iput-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->P0:Landroid/view/accessibility/AccessibilityNodeInfo;

    new-instance p1, Ljava/lang/StringBuilder;

    const-string v7, "EDITTEXT: "

    invoke-direct {p1, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v7, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->O0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {v8, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    new-instance p1, Ljava/lang/StringBuilder;

    const-string v7, "SUBMIT: "

    invoke-direct {p1, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v7, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->P0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {v8, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const-string p1, "clipboard"

    invoke-virtual {p0, p1}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/content/ClipboardManager;

    invoke-virtual {p1}, Landroid/content/ClipboardManager;->getPrimaryClip()Landroid/content/ClipData;

    move-result-object p1

    if-eqz p1, :cond_12

    invoke-virtual {p1, v5}, Landroid/content/ClipData;->getItemAt(I)Landroid/content/ClipData$Item;

    move-result-object p1

    invoke-virtual {p1}, Landroid/content/ClipData$Item;->getText()Ljava/lang/CharSequence;

    move-result-object p1

    invoke-interface {p1}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object p1

    const-string v7, "[0-9]{6}"

    invoke-virtual {p1, v7}, Ljava/lang/String;->matches(Ljava/lang/String;)Z

    move-result v7

    if-eqz v7, :cond_12

    new-instance p0, Landroid/os/Bundle;

    invoke-direct {p0}, Landroid/os/Bundle;-><init>()V

    invoke-virtual {p0, v6, p1}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    invoke-virtual {v1, v0, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    invoke-interface {v9, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_12
    sget-boolean p1, Lcom/tatkal/train/quick/TabActivity2;->v3:Z

    if-eqz p1, :cond_1e

    sget p1, Lcom/tatkal/train/quick/SplashActivity;->u:I

    if-ne p1, v2, :cond_1e

    const-string p1, "STARTING OTP TIMER"

    invoke-static {v8, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q0:I

    iget-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->N0:Ljava/util/Timer;

    if-nez p1, :cond_1e

    new-instance p1, Ljava/util/Timer;

    invoke-direct {p1}, Ljava/util/Timer;-><init>()V

    iput-object p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->N0:Ljava/util/Timer;

    new-instance v7, Loh1;

    invoke-direct {v7, p0, v5}, Loh1;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;I)V

    iput v5, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->R0:I

    iget-object v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->N0:Ljava/util/Timer;

    const-wide/16 v8, 0x0

    const-wide/16 v10, 0xc8

    invoke-virtual/range {v6 .. v11}, Ljava/util/Timer;->schedule(Ljava/util/TimerTask;JJ)V

    return-void

    :cond_13
    iget v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->i1:I

    if-ne v0, v10, :cond_1e

    const-string v0, "Resend OTP"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-nez v0, :cond_1e

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->p(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    return-void

    :pswitch_2
    iget v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->j1:I

    if-nez v0, :cond_15

    const-string v0, "Wallet (One-click Payment)"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "Mobikwik Wallet"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_14

    invoke-static {v4, p1}, Lyi;->e(ILjava/util/List;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->j1:I

    goto/16 :goto_2

    :cond_14
    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result p1

    if-nez p1, :cond_1e

    :try_start_4
    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->j1:I
    :try_end_4
    .catch Ljava/lang/Exception; {:try_start_4 .. :try_end_4} :catch_2

    goto/16 :goto_2

    :cond_15
    if-ne v0, v4, :cond_16

    const-string v0, "PAY USING WALLET"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_1e

    const-string v0, "Mobikwik"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    :try_start_5
    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    sub-int/2addr v0, v4

    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->j1:I
    :try_end_5
    .catch Ljava/lang/Exception; {:try_start_5 .. :try_end_5} :catch_2

    goto/16 :goto_2

    :cond_16
    if-ne v0, v2, :cond_1e

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "ADDL. CHARGES"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_17

    :try_start_6
    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v11, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->j1:I
    :try_end_6
    .catch Ljava/lang/Exception; {:try_start_6 .. :try_end_6} :catch_2

    goto/16 :goto_2

    :cond_17
    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result p1

    if-nez p1, :cond_1e

    :try_start_7
    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v11, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->j1:I
    :try_end_7
    .catch Ljava/lang/Exception; {:try_start_7 .. :try_end_7} :catch_2

    goto/16 :goto_2

    :pswitch_3
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q1:Z

    const-string v2, "Razorpay UPI"

    if-nez v0, :cond_18

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v6

    if-nez v6, :cond_18

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q1:Z

    :cond_18
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->Q1:Z

    if-eqz v0, :cond_19

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->R1:Z

    if-nez v0, :cond_19

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_19

    invoke-interface {v0, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->R1:Z

    :cond_19
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->R1:Z

    const-string v1, "PAY \u20b9"

    if-eqz v0, :cond_1b

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S1:Z

    if-nez v0, :cond_1b

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    const-string v2, "Paytm"

    invoke-virtual {v0, v8, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v2, "PhonePe"

    invoke-virtual {v0, v9, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v2, "CRED UPI"

    invoke-virtual {v0, v7, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->isEmpty()Z

    move-result v6

    if-nez v6, :cond_1b

    invoke-interface {v2, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v7

    invoke-interface {v7}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v7

    const-string v8, "TextView"

    invoke-virtual {v7, v8}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v7

    if-nez v7, :cond_1a

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v7

    if-le v7, v4, :cond_1a

    invoke-interface {v2, v4}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    move-object v6, v2

    check-cast v6, Landroid/view/accessibility/AccessibilityNodeInfo;

    move v2, v4

    goto :goto_1

    :cond_1a
    move v2, v5

    :goto_1
    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {v6, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget-object v6, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->u0:Ljava/lang/String;

    invoke-virtual {v0, v6}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v6

    if-nez v6, :cond_1b

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-le v6, v2, :cond_1b

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_1b

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S1:Z

    :cond_1b
    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->S1:Z

    if-eqz v0, :cond_1e

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->T1:Z

    if-nez v0, :cond_1e

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_1e

    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->T1:Z

    return-void

    :pswitch_4
    iget v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->k1:I

    if-nez v0, :cond_1c

    const-string v0, "Wallet (Instant Payment)"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_1e

    :try_start_8
    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->k1:I
    :try_end_8
    .catch Ljava/lang/Exception; {:try_start_8 .. :try_end_8} :catch_2

    goto :goto_2

    :cond_1c
    if-ne v0, v4, :cond_1d

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_1e

    :try_start_9
    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->k1:I
    :try_end_9
    .catch Ljava/lang/Exception; {:try_start_9 .. :try_end_9} :catch_2

    goto :goto_2

    :cond_1d
    if-ne v0, v2, :cond_1e

    const-string v0, "Available balance in wallet"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_1e

    const-string v0, "Pay Now"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    :try_start_a
    invoke-interface {p1, v5}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput v10, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->k1:I
    :try_end_a
    .catch Ljava/lang/Exception; {:try_start_a .. :try_end_a} :catch_2

    :catch_2
    :cond_1e
    :goto_2
    return-void

    nop

    :sswitch_data_0
    .sparse-switch
        -0x31145198 -> :sswitch_6
        0xe444785 -> :sswitch_5
        0x2cad3cb6 -> :sswitch_4
        0x4a535943 -> :sswitch_3
        0x68089340 -> :sswitch_2
        0x6d606690 -> :sswitch_1
        0x75fd04cc -> :sswitch_0
    .end sparse-switch

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_3
        :pswitch_1
        :pswitch_0
        :pswitch_3
    .end packed-switch
.end method

.method public final t(Ljava/util/List;Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 7

    const-string v0, "CANCEL"

    invoke-virtual {p3, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object p3

    const/4 v0, 0x0

    invoke-interface {p3, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v1

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v1

    const/4 v2, 0x1

    sub-int/2addr v1, v2

    iget-object v3, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    const-string v4, "-"

    invoke-virtual {v3, v4}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    aget-object v3, v3, v0

    invoke-static {v3}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v3

    const/4 v5, 0x2

    :try_start_0
    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    invoke-virtual {p2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    :goto_0
    iget-object p2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    invoke-virtual {p2, v4}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p2

    aget-object p2, p2, v2

    invoke-static {p2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p2

    sub-int/2addr p2, v2

    iget-object v4, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->O:[Ljava/lang/String;

    aget-object p2, v4, p2

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v4

    sub-int/2addr v4, v2

    move v5, v0

    :goto_1
    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v6

    if-ge v5, v6, :cond_1

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {v6, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->getContentDescription()Ljava/lang/CharSequence;

    move-result-object v6

    invoke-interface {v6}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v6, p2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v6

    if-eqz v6, :cond_0

    move v4, v5

    goto :goto_2

    :cond_0
    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_1
    :goto_2
    invoke-virtual {p1, v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    sub-int/2addr v3, v2

    invoke-virtual {p1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    const/16 p2, 0x10

    invoke-virtual {p1, p2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    invoke-interface {p3, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object p1

    invoke-virtual {p1, p2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v2, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    iget-object p0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    const/4 p1, 0x5

    aput-boolean v2, p0, p1

    return-void
.end method

.method public final u(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 20

    move-object/from16 v1, p0

    move-object/from16 v0, p1

    move-object/from16 v2, p2

    new-instance v3, Ljava/lang/StringBuilder;

    const-string v4, "cris.org.in.prs.ima:id/"

    invoke-direct {v3, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->U:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v5

    const/4 v8, 0x4

    const/4 v9, 0x2

    const-string v10, " - "

    const/16 v11, 0x10

    const/4 v12, 0x1

    const/4 v13, 0x0

    if-lez v5, :cond_4

    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    if-nez v5, :cond_4

    iput v8, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const-string v5, "cris.org.in.prs.ima:id/fromStn_code"

    invoke-virtual {v0, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    const-string v14, "cris.org.in.prs.ima:id/toStn_code"

    invoke-virtual {v0, v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v14

    invoke-interface {v5, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v5

    invoke-interface {v5}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-interface {v14, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v14

    invoke-interface {v14}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v14

    iget-object v15, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    invoke-virtual {v15, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v15

    aget-object v15, v15, v12

    const-wide/16 v16, 0x3e8

    iget-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    invoke-virtual {v6, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v6

    aget-object v6, v6, v12

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v7

    move/from16 v18, v8

    const-string v8, "cris.org.in.prs.ima:id/stn_rotation"

    if-eqz v7, :cond_1

    invoke-virtual {v0, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-virtual {v14, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_0

    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto :goto_0

    :cond_0
    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :try_start_0
    invoke-static/range {v16 .. v17}, Ljava/lang/Thread;->sleep(J)V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->U:Ljava/lang/String;

    iget-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V:Ljava/lang/String;

    iput-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->U:Ljava/lang/String;

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V:Ljava/lang/String;

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    iget-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    iput-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    return-void

    :cond_1
    invoke-virtual {v5, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    invoke-virtual {v14, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    iput v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    goto :goto_0

    :cond_2
    invoke-virtual {v14, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-virtual {v0, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_3
    :try_start_1
    invoke-static/range {v16 .. v17}, Ljava/lang/Thread;->sleep(J)V
    :try_end_1
    .catch Ljava/lang/InterruptedException; {:try_start_1 .. :try_end_1} :catch_1

    :catch_1
    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_4
    move/from16 v18, v8

    const-wide/16 v16, 0x3e8

    :goto_0
    const-string v3, "cris.org.in.prs.ima:id/tv_search_text"

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v5

    iget-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    if-lez v5, :cond_8

    const/4 v2, 0x5

    iput v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    const/high16 v4, 0x200000

    const-string v5, "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE"

    if-nez v2, :cond_7

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    invoke-virtual {v6, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v6

    aget-object v6, v6, v13

    invoke-virtual {v2, v5, v6}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3, v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    iget-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X:Ljava/lang/String;

    invoke-virtual {v2, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v2

    aget-object v2, v2, v13

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v2

    :cond_5
    :goto_1
    invoke-interface {v2}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_6

    invoke-interface {v2}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    :try_start_2
    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    const-string v4, "RecyclerView"

    invoke-virtual {v3, v4}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v3
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_2

    if-eqz v3, :cond_5

    goto :goto_2

    :catch_2
    move-exception v0

    new-instance v3, Ljava/lang/StringBuilder;

    const-string v4, "STN ERROR: "

    invoke-direct {v3, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v3, "STUDIOS"

    invoke-static {v3, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_1

    :cond_6
    const/4 v0, 0x0

    :goto_2
    if-eqz v0, :cond_19

    iput v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto/16 :goto_5

    :cond_7
    if-ne v2, v12, :cond_19

    new-instance v2, Landroid/os/Bundle;

    invoke-direct {v2}, Landroid/os/Bundle;-><init>()V

    iget-object v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    invoke-virtual {v7, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v7

    aget-object v7, v7, v13

    invoke-virtual {v2, v5, v7}, Landroid/os/Bundle;->putCharSequence(Ljava/lang/String;Ljava/lang/CharSequence;)V

    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3, v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z

    iget-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y:Ljava/lang/String;

    invoke-virtual {v2, v10}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v2

    const-string v3, "cris.org.in.prs.ima:id/rv_station_list"

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    aget-object v3, v2, v12

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_19

    invoke-interface {v0, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    aget-object v2, v2, v12

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_19

    iput v9, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    invoke-interface {v0, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    aput-boolean v12, v6, v18

    return-void

    :cond_8
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_9

    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    if-ne v5, v12, :cond_9

    :try_start_3
    invoke-static/range {v16 .. v17}, Ljava/lang/Thread;->sleep(J)V
    :try_end_3
    .catch Ljava/lang/InterruptedException; {:try_start_3 .. :try_end_3} :catch_3

    :catch_3
    invoke-interface {v3, v13}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_9
    const-string v3, "cris.org.in.prs.ima:id/select_journey_date"

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    const-string v5, "cris.org.in.prs.ima:id/tomorrow_ll"

    invoke-virtual {v0, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    const-string v7, "cris.org.in.prs.ima:id/dayaftertomorrow_ll"

    invoke-virtual {v0, v7}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v7

    const-string v8, "cris.org.in.prs.ima:id/twodaysaftertomorrow_ll"

    invoke-virtual {v0, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v8

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v10

    if-lez v10, :cond_d

    iget-boolean v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    if-nez v10, :cond_d

    iget-boolean v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    if-nez v10, :cond_d

    move/from16 v10, v18

    iput v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v0

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v2

    new-instance v4, Ljava/text/SimpleDateFormat;

    const-string v6, "dd-MM-yyyy"

    sget-object v10, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-direct {v4, v6, v10}, Ljava/text/SimpleDateFormat;-><init>(Ljava/lang/String;Ljava/util/Locale;)V

    :try_start_4
    invoke-virtual {v2}, Ljava/util/Calendar;->getTime()Ljava/util/Date;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/text/DateFormat;->format(Ljava/util/Date;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/text/DateFormat;->parse(Ljava/lang/String;)Ljava/util/Date;

    move-result-object v6

    invoke-virtual {v0, v6}, Ljava/util/Calendar;->setTime(Ljava/util/Date;)V

    iget-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    invoke-virtual {v4, v6}, Ljava/text/DateFormat;->parse(Ljava/lang/String;)Ljava/util/Date;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/util/Calendar;->setTime(Ljava/util/Date;)V

    invoke-virtual {v2}, Ljava/util/Calendar;->getTime()Ljava/util/Date;

    move-result-object v2

    invoke-virtual {v2}, Ljava/util/Date;->getTime()J

    move-result-wide v15

    invoke-virtual {v0}, Ljava/util/Calendar;->getTime()Ljava/util/Date;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/Date;->getTime()J

    move-result-wide v17
    :try_end_4
    .catch Ljava/lang/Exception; {:try_start_4 .. :try_end_4} :catch_5

    sub-long v15, v15, v17

    const-wide/32 v17, 0x5265c00

    :try_start_5
    div-long v13, v15, v17

    long-to-int v0, v13

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    if-eq v0, v12, :cond_c

    if-eq v0, v9, :cond_b

    const/4 v2, 0x3

    if-eq v0, v2, :cond_a

    const/4 v10, 0x0

    iput-boolean v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    invoke-interface {v3, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->I0:Z
    :try_end_5
    .catch Ljava/lang/Exception; {:try_start_5 .. :try_end_5} :catch_4

    return-void

    :catch_4
    const/4 v10, 0x0

    goto :goto_3

    :cond_a
    const/4 v10, 0x0

    :try_start_6
    invoke-interface {v8, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_b
    const/4 v10, 0x0

    invoke-interface {v7, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z
    :try_end_6
    .catch Ljava/lang/Exception; {:try_start_6 .. :try_end_6} :catch_6

    return-void

    :cond_c
    const/4 v10, 0x0

    :try_start_7
    invoke-interface {v5, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z
    :try_end_7
    .catch Ljava/lang/Exception; {:try_start_7 .. :try_end_7} :catch_4

    return-void

    :catch_5
    move v10, v13

    :catch_6
    :goto_3
    iput-boolean v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    invoke-interface {v3, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->I0:Z

    return-void

    :cond_d
    const-string v3, "CANCEL"

    invoke-virtual {v0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v3

    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z0:Z

    const/4 v7, 0x6

    if-eqz v5, :cond_14

    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    if-lez v5, :cond_14

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v5

    if-gtz v5, :cond_e

    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    if-eqz v5, :cond_14

    :cond_e
    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    if-nez v5, :cond_14

    iput v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    const/16 v5, 0x63

    if-ne v4, v5, :cond_10

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v4

    invoke-virtual {v4, v9}, Ljava/util/Calendar;->get(I)I

    move-result v4

    add-int/2addr v4, v12

    iget-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    const-string v6, "-"

    invoke-virtual {v5, v6}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v5

    aget-object v5, v5, v12

    invoke-static {v5}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v5

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v7

    invoke-virtual {v7, v12}, Ljava/util/Calendar;->get(I)I

    move-result v7

    iget-object v8, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->P:Ljava/lang/String;

    invoke-virtual {v8, v6}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v6

    aget-object v6, v6, v9

    invoke-static {v6}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v6

    if-le v6, v7, :cond_f

    add-int/lit8 v5, v5, 0xc

    :cond_f
    sub-int/2addr v5, v4

    iput v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    :cond_10
    iget-boolean v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    if-nez v4, :cond_11

    const/4 v10, 0x0

    invoke-interface {v3, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    goto :goto_4

    :cond_11
    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    :goto_4
    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    invoke-virtual {v4, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    if-nez v5, :cond_12

    iget-boolean v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s:Z

    if-nez v6, :cond_12

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s:Z

    :cond_12
    if-lez v5, :cond_13

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v5

    sub-int/2addr v5, v12

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    invoke-virtual {v4, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    sub-int/2addr v4, v12

    iput v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    if-nez v4, :cond_19

    const/4 v10, 0x0

    iput-boolean v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    :try_start_8
    invoke-virtual {v1, v3, v2, v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->t(Ljava/util/List;Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    :try_end_8
    .catch Ljava/lang/Exception; {:try_start_8 .. :try_end_8} :catch_7

    :catch_7
    return-void

    :cond_13
    const/4 v10, 0x0

    iput-boolean v10, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    iget-boolean v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s:Z

    if-eqz v4, :cond_19

    invoke-virtual {v1, v3, v2, v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->t(Ljava/util/List;Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    return-void

    :cond_14
    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    if-eqz v5, :cond_15

    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s:Z

    if-nez v5, :cond_15

    iput v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    invoke-virtual {v1, v3, v2, v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->t(Ljava/util/List;Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/accessibility/AccessibilityNodeInfo;)V

    return-void

    :cond_15
    const-string v2, "cris.org.in.prs.ima:id/flexible_date"

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_16

    iget-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c1:Z

    if-nez v2, :cond_16

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c1:Z

    :cond_16
    const-string v2, "cris.org.in.prs.ima:id/ll_quota"

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    new-instance v3, Ljava/util/HashMap;

    invoke-direct {v3}, Ljava/util/HashMap;-><init>()V

    iput-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "GN"

    const-string v7, "tv_general"

    invoke-virtual {v3, v5, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "LD"

    const-string v8, "tv_ladies"

    invoke-virtual {v3, v5, v8}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "TQ"

    const-string v8, "tv_tatkal"

    invoke-virtual {v3, v5, v8}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "SS"

    const-string v8, "tv_senior_citizen"

    invoke-virtual {v3, v5, v8}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "PT"

    const-string v8, "tv_premium_tatkal"

    invoke-virtual {v3, v5, v8}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "HP"

    const-string v8, "tv_ph_handicap"

    invoke-virtual {v3, v5, v8}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    const-string v5, "DP"

    invoke-virtual {v3, v5, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_18

    iget-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->E:Z

    if-nez v3, :cond_18

    const/4 v3, 0x4

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v10, 0x0

    invoke-interface {v2, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Q:Ljava/util/HashMap;

    iget-object v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_17

    const/4 v10, 0x0

    invoke-interface {v0, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_17
    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->E:Z

    :cond_18
    const-string v0, "tv_search"

    invoke-virtual {v1, v0}, Lcom/tatkal/train/quick/MyAccessibilityService;->g(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    if-eqz v0, :cond_19

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_19

    iget-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    if-eqz v2, :cond_19

    iget-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c1:Z

    if-eqz v2, :cond_19

    iget-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->E:Z

    if-eqz v2, :cond_19

    iget-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->F:Z

    if-nez v2, :cond_19

    const/4 v3, 0x4

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v10, 0x0

    invoke-interface {v0, v10}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v12, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->F:Z

    const/16 v19, 0x3

    aput-boolean v12, v6, v19

    :cond_19
    :goto_5
    return-void
.end method

.method public final v(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 23

    move-object/from16 v1, p0

    move-object/from16 v0, p1

    const-string v2, ""

    sput-object v2, Lcom/tatkal/train/quick/MyAccessibilityService;->e2:Ljava/lang/String;

    const/4 v3, 0x0

    invoke-static {v0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->w(Landroid/view/accessibility/AccessibilityNodeInfo;I)V

    const-string v4, "ScrollView"

    invoke-virtual {v1, v0, v4}, Lcom/tatkal/train/quick/MyAccessibilityService;->f(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z

    const/4 v6, 0x2

    if-eqz v5, :cond_0

    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    if-ne v5, v6, :cond_0

    goto/16 :goto_9

    :cond_0
    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    const/4 v7, 0x1

    if-ne v5, v7, :cond_1

    iput v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    :cond_1
    iget-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->t1:Z

    const-string v8, "cris.org.in.prs.ima:id/tv_continue"

    const-string v9, "cris.org.in.prs.ima:id/tv_avl_detail"

    const/16 v10, 0x10

    if-eqz v5, :cond_5

    iget v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M0:I

    if-ne v2, v7, :cond_2

    goto/16 :goto_9

    :cond_2
    iget v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    invoke-virtual {v4, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v2

    invoke-virtual {v2, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v4

    if-lez v4, :cond_3

    invoke-interface {v2, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    iput-object v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-interface {v2, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v2

    invoke-interface {v2}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v2

    iput-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    :cond_3
    invoke-virtual {v0, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y0:Landroid/view/accessibility/AccessibilityNodeInfo;

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    iput v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->X0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iget-boolean v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    if-nez v0, :cond_4

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Y0:Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    :cond_4
    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    iget-object v1, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    return-void

    :cond_5
    const-string v5, "cris.org.in.prs.ima:id/lv_train_list_aternate"

    invoke-virtual {v0, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v5

    new-instance v11, Ljava/lang/StringBuilder;

    const-string v12, "trainsLL size: "

    invoke-direct {v11, v12}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v12

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    const-string v12, "Train selection"

    invoke-static {v12, v11}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v11

    const/4 v13, 0x7

    if-lez v11, :cond_17

    iget v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    if-nez v11, :cond_17

    iput v13, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    iget-object v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    const-string v14, "Selecting train"

    invoke-virtual {v11, v14}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->G:I

    const/16 v11, 0x63

    iput v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->e:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->z0:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->E:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->F:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->I0:Z

    new-instance v11, Ljava/lang/StringBuilder;

    const-string v14, "trainList children: "

    invoke-direct {v11, v14}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v14

    invoke-virtual {v11, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v12, v11}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    move v11, v3

    :goto_0
    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v14

    if-ge v11, v14, :cond_17

    invoke-virtual {v4, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v14

    iget-object v15, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b0:Ljava/lang/String;

    invoke-virtual {v14, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v14

    invoke-interface {v14}, Ljava/util/List;->size()I

    move-result v14

    if-lez v14, :cond_16

    new-instance v14, Ljava/lang/StringBuilder;

    const-string v15, "Train "

    invoke-direct {v14, v15}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v15, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b0:Ljava/lang/String;

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v15, " found at: "

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v11}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v14

    invoke-static {v12, v14}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    invoke-virtual {v4, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v14

    const-string v15, "cris.org.in.prs.ima:id/rv_class_fare_avl"

    invoke-virtual {v14, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v14

    invoke-interface {v14, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/view/accessibility/AccessibilityNodeInfo;

    iget-object v15, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    invoke-virtual {v14, v15}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    invoke-virtual {v13, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " class count "

    invoke-virtual {v13, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-interface {v15}, Ljava/util/List;->size()I

    move-result v6

    invoke-virtual {v13, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v12, v6}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-interface {v15}, Ljava/util/List;->size()I

    move-result v6

    iget-object v13, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s0:Ljava/util/HashMap;

    if-nez v6, :cond_7

    iget-object v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    invoke-virtual {v13, v6}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    invoke-virtual {v14, v6}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v15

    invoke-interface {v15}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_6

    const-string v6, "Updated class found."

    invoke-static {v12, v6}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput-boolean v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->t1:Z

    goto :goto_1

    :cond_6
    const-string v0, "Updated class not found. Scrolling forward"

    invoke-static {v12, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/16 v0, 0x1000

    invoke-virtual {v14, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_7
    :goto_1
    move v6, v3

    :goto_2
    invoke-interface {v15}, Ljava/util/List;->size()I

    move-result v14

    if-ge v6, v14, :cond_15

    invoke-interface {v15, v6}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v14

    invoke-interface {v14}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v14}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v14

    iget-object v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    invoke-virtual {v13, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v14, v3}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v3

    invoke-interface {v15, v6}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v14}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v14

    invoke-interface {v14}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v14}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v14

    iget-object v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    invoke-virtual {v14, v7}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v7

    if-nez v7, :cond_9

    if-nez v3, :cond_9

    iget-boolean v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->t1:Z

    if-eqz v7, :cond_8

    goto :goto_3

    :cond_8
    add-int/lit8 v6, v6, 0x1

    const/4 v3, 0x0

    const/4 v7, 0x1

    goto :goto_2

    :cond_9
    :goto_3
    invoke-interface {v15, v6}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v2, 0x1

    iput v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    new-instance v0, Ljava/lang/StringBuilder;

    const-string v2, "Class ["

    invoke-direct {v0, v2}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-interface {v15, v6}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getViewIdResourceName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "] clicked"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v12, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v2, "RC Train class click"

    invoke-virtual {v0, v2}, Lkf1;->m(Ljava/lang/String;)V

    if-eqz v3, :cond_a

    const/4 v2, 0x1

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->t1:Z

    return-void

    :cond_a
    invoke-interface {v15, v6}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    new-instance v2, Ljava/lang/StringBuilder;

    const-string v5, "avlTxtViewId size: "

    invoke-direct {v2, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v12, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    if-lez v2, :cond_b

    const/4 v2, 0x0

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v2

    invoke-interface {v2}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v2

    iput-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    new-instance v2, Ljava/lang/StringBuilder;

    const-string v5, "Availability: "

    invoke-direct {v2, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v12, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v2, 0x0

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    invoke-virtual {v0, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v2, 0x1

    iput v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    goto :goto_4

    :cond_b
    const/4 v2, 0x1

    :goto_4
    iget-boolean v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z

    if-eqz v0, :cond_d

    :cond_c
    const/4 v2, 0x0

    goto/16 :goto_6

    :cond_d
    iget-boolean v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->T0:Z

    if-nez v0, :cond_c

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->T0:Z

    new-instance v0, Ljava/util/Timer;

    invoke-direct {v0}, Ljava/util/Timer;-><init>()V

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->J0:Ljava/util/Timer;

    new-instance v0, Loh1;

    invoke-direct {v0, v1, v2}, Loh1;-><init>(Lcom/tatkal/train/quick/MyAccessibilityService;I)V

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->K0:Loh1;

    const/16 v0, 0x8

    iput v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s1:I

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    const-string v2, "TQ"

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/16 v2, 0xb

    if-nez v0, :cond_e

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->Z:Ljava/lang/String;

    const-string v5, "PT"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_10

    :cond_e
    const/16 v0, 0xa

    iput v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s1:I

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    const-string v5, "2S"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_f

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    const-string v5, "FC"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_f

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->c0:Ljava/lang/String;

    const-string v5, "SL"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_10

    :cond_f
    iput v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s1:I

    :cond_10
    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v0

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v5

    iget v7, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->s1:I

    const/16 v16, 0x1

    add-int/lit8 v7, v7, -0x1

    invoke-virtual {v5, v2, v7}, Ljava/util/Calendar;->set(II)V

    const/16 v2, 0xc

    const/16 v7, 0x3b

    invoke-virtual {v5, v2, v7}, Ljava/util/Calendar;->set(II)V

    const/16 v2, 0xd

    invoke-virtual {v5, v2, v7}, Ljava/util/Calendar;->set(II)V

    :try_start_0
    iget v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M0:I

    if-nez v2, :cond_c

    invoke-virtual {v5}, Ljava/util/Calendar;->getTimeInMillis()J

    invoke-virtual {v0}, Ljava/util/Calendar;->getTimeInMillis()J

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->J0:Ljava/util/Timer;

    iget-object v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->K0:Loh1;

    const-wide/16 v21, 0x1f4

    const-wide/16 v19, 0x1388

    move-object/from16 v17, v0

    move-object/from16 v18, v2

    invoke-virtual/range {v17 .. v22}, Ljava/util/Timer;->schedule(Ljava/util/TimerTask;JJ)V

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    const-string v2, "Please wait"

    invoke-virtual {v0, v2}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1

    const/4 v2, 0x1

    :try_start_1
    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    const/4 v2, 0x1

    goto :goto_6

    :catch_0
    move-exception v0

    const/4 v2, 0x1

    goto :goto_5

    :catch_1
    move-exception v0

    const/4 v2, 0x0

    :goto_5
    invoke-virtual {v0}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->J0:Ljava/util/Timer;

    if-eqz v0, :cond_11

    invoke-virtual {v0}, Ljava/util/Timer;->cancel()V

    const/4 v0, 0x0

    iput-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->J0:Ljava/util/Timer;

    :cond_11
    :goto_6
    if-nez v2, :cond_12

    iget v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M0:I

    const/4 v5, 0x1

    if-ne v0, v5, :cond_1a

    :cond_12
    if-eqz v2, :cond_13

    iget v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->M0:I

    const/4 v7, 0x2

    if-eq v0, v7, :cond_1a

    :cond_13
    invoke-virtual {v4, v11}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v0

    const-string v2, "cris.org.in.prs.ima:id/tv_otherdate"

    invoke-virtual {v0, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    new-instance v2, Ljava/lang/StringBuilder;

    const-string v4, "otherDates size: "

    invoke-direct {v2, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " | b: "

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v12, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    if-gtz v2, :cond_14

    if-eqz v3, :cond_1a

    :cond_14
    const/4 v2, 0x1

    iput-boolean v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->S0:Z

    iput v11, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    iput v6, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->W0:I

    if-nez v3, :cond_1a

    const/4 v2, 0x0

    iput v2, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L:I

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    goto/16 :goto_9

    :cond_15
    const/4 v7, 0x2

    goto :goto_7

    :cond_16
    move v7, v6

    :goto_7
    add-int/lit8 v11, v11, 0x1

    move v6, v7

    const/4 v3, 0x0

    const/4 v7, 0x1

    const/4 v13, 0x7

    goto/16 :goto_0

    :cond_17
    invoke-virtual {v0, v8}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v3

    if-lez v3, :cond_1a

    const/4 v3, 0x7

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->b1:I

    const/4 v3, 0x0

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->A:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->B:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->N:Z

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->H:Z

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->I:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->J:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->f:Z

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->x:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->y:I

    iput-boolean v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->K:Z

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->w:I

    iput v3, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->x0:I

    invoke-interface {v5}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_18

    iget v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->V0:I

    invoke-virtual {v4, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v4

    invoke-virtual {v4, v9}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;

    move-result-object v4

    invoke-interface {v4}, Ljava/util/List;->size()I

    move-result v5

    if-lez v5, :cond_18

    invoke-interface {v4, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v4}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v4

    invoke-interface {v4}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v4

    iput-object v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    :cond_18
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    iget-boolean v4, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    if-nez v4, :cond_19

    invoke-interface {v0, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v0, v10}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    const/4 v5, 0x1

    iput-boolean v5, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->u1:Z

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->L1:Lkf1;

    const-string v3, "RC Search Train click"

    invoke-virtual {v0, v3}, Lkf1;->m(Ljava/lang/String;)V

    goto :goto_8

    :cond_19
    const/4 v5, 0x1

    :goto_8
    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->d1:[Z

    const/4 v3, 0x6

    aput-boolean v5, v0, v3

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1a

    iget-object v0, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    iget-object v1, v1, Lcom/tatkal/train/quick/MyAccessibilityService;->a1:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lcom/tatkal/train/quick/FloatingWidgetService;->c(Ljava/lang/String;)V

    :cond_1a
    :goto_9
    return-void
.end method

.method public final x(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 7

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->G1:Z

    if-nez v0, :cond_1

    invoke-virtual {p1}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v0

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/MyAccessibilityService;->y(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    if-ge v2, v0, :cond_1

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v3

    invoke-virtual {p0, v3}, Lcom/tatkal/train/quick/MyAccessibilityService;->y(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    invoke-virtual {v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I

    move-result v4

    if-lez v4, :cond_0

    move v5, v1

    :goto_1
    if-ge v5, v4, :cond_0

    invoke-virtual {v3, v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;

    move-result-object v6

    invoke-virtual {p0, v6}, Lcom/tatkal/train/quick/MyAccessibilityService;->x(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_0
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public final y(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 8

    const-string v0, "You are transferring"

    invoke-virtual {p1, v0}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    const-string v1, "You are SENDING"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    const-string v2, "Enter your PIN"

    invoke-virtual {p1, v2}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-gtz v0, :cond_0

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v0

    if-gtz v0, :cond_0

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_a

    :cond_0
    sget-object v0, Ljg;->l:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x3

    if-eq v0, v1, :cond_1

    sget-object v0, Ljg;->l:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x5

    if-ne v0, v1, :cond_2

    :cond_1
    new-instance v0, Ljava/lang/StringBuilder;

    const-string v1, "0"

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v1, Ljg;->l:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    sput-object v0, Ljg;->l:Ljava/lang/String;

    :cond_2
    const/4 v0, 0x0

    move v1, v0

    :goto_0
    sget-object v2, Ljg;->l:Ljava/lang/String;

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v2

    const/16 v3, 0x10

    if-ge v1, v2, :cond_5

    sget-object v2, Ljg;->l:Ljava/lang/String;

    add-int/lit8 v4, v1, 0x1

    invoke-virtual {v2, v1, v4}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    if-eqz v2, :cond_4

    invoke-interface {v2}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v2

    :cond_3
    invoke-interface {v2}, Ljava/util/Iterator;->hasNext()Z

    move-result v5

    if-eqz v5, :cond_4

    invoke-interface {v2}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    const-string v6, "android.widget.TextView"

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/String;->contentEquals(Ljava/lang/CharSequence;)Z

    move-result v6

    if-eqz v6, :cond_3

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v6

    invoke-interface {v6}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v6, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_3

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->isClickable()Z

    move-result v6

    if-eqz v6, :cond_3

    invoke-virtual {v5, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    :cond_4
    move v1, v4

    goto :goto_0

    :cond_5
    const-string v1, "Pay"

    invoke-virtual {p1, v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;

    move-result-object v2

    move v4, v0

    :goto_1
    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v5

    if-ge v4, v5, :cond_7

    invoke-interface {v2, v4}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v5}, Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;

    move-result-object v5

    invoke-interface {v5}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5, v1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v5

    if-eqz v5, :cond_6

    invoke-interface {v2, v4}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {p0, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    return-void

    :cond_6
    add-int/lit8 v4, v4, 0x1

    goto :goto_1

    :cond_7
    new-instance v1, Ljava/util/ArrayList;

    invoke-direct {v1}, Ljava/util/ArrayList;-><init>()V

    invoke-static {p1, v1}, Lcom/tatkal/train/quick/MyAccessibilityService;->d(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/util/ArrayList;)V

    invoke-virtual {v1}, Ljava/util/ArrayList;->isEmpty()Z

    move-result p1

    if-nez p1, :cond_8

    const/4 p1, 0x1

    invoke-static {v1, p1}, Lyi;->f(Ljava/util/ArrayList;I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/accessibility/AccessibilityNodeInfo;

    invoke-virtual {v1}, Landroid/view/accessibility/AccessibilityNodeInfo;->isClickable()Z

    move-result v2

    if-eqz v2, :cond_8

    invoke-virtual {v1, v3}, Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z

    iput-boolean p1, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->G1:Z

    sput-boolean v0, Ljg;->j:Z

    sput-boolean v0, Ljg;->i:Z

    sput-boolean v0, Ljg;->k:Z

    :cond_8
    sget p1, Lcom/tatkal/train/quick/SplashActivity;->u:I

    const/4 v0, 0x2

    if-eq p1, v0, :cond_9

    sget-object p1, Lk6;->o:Ljava/lang/String;

    const-string v1, "N"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_9

    new-instance p1, Laf0;

    invoke-direct {p1, p0}, Laf0;-><init>(Landroid/content/Context;)V

    iget-object p0, p1, Laf0;->a:Lcom/google/firebase/firestore/FirebaseFirestore;

    sget-object p1, Ljg;->R:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lcom/google/firebase/firestore/FirebaseFirestore;->a(Ljava/lang/String;)Lqo;

    move-result-object p0

    sget-object p1, Lcom/tatkal/train/quick/SplashActivity;->A:Ljava/lang/String;

    invoke-static {p1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p1

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p1

    const-string v1, "tid"

    invoke-virtual {p0, p1, v1}, Llu1;->g(Ljava/lang/Object;Ljava/lang/String;)Llu1;

    move-result-object p0

    invoke-virtual {p0, v0}, Llu1;->a(I)Lcom/google/android/gms/tasks/Task;

    move-result-object p0

    new-instance p1, Lya0;

    const/16 v0, 0x17

    invoke-direct {p1, v0}, Lya0;-><init>(I)V

    invoke-virtual {p0, p1}, Lcom/google/android/gms/tasks/Task;->addOnCompleteListener(Lcom/google/android/gms/tasks/OnCompleteListener;)Lcom/google/android/gms/tasks/Task;

    return-void

    :cond_9
    sget-boolean p1, Ljg;->h:Z

    if-eqz p1, :cond_a

    iget-object p0, p0, Lcom/tatkal/train/quick/MyAccessibilityService;->a:Lcom/tatkal/train/quick/FloatingWidgetService;

    invoke-virtual {p0}, Lcom/tatkal/train/quick/FloatingWidgetService;->e()V

    :cond_a
    return-void
.end method
