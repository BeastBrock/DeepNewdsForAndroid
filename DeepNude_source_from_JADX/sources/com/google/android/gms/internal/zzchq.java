package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.Hide;

@Hide
public final class zzchq implements Creator<zzchp> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzd = zzbgm.zzd(parcel);
        double d = 0.0d;
        double d2 = d;
        String str = null;
        long j = 0;
        int i = 0;
        short s = (short) 0;
        float f = 0.0f;
        int i2 = 0;
        int i3 = -1;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case 1:
                    str = zzbgm.zzq(parcel2, readInt);
                    break;
                case 2:
                    j = zzbgm.zzi(parcel2, readInt);
                    break;
                case 3:
                    s = zzbgm.zzf(parcel2, readInt);
                    break;
                case 4:
                    d = zzbgm.zzn(parcel2, readInt);
                    break;
                case 5:
                    d2 = zzbgm.zzn(parcel2, readInt);
                    break;
                case 6:
                    f = zzbgm.zzl(parcel2, readInt);
                    break;
                case 7:
                    i = zzbgm.zzg(parcel2, readInt);
                    break;
                case 8:
                    i2 = zzbgm.zzg(parcel2, readInt);
                    break;
                case 9:
                    i3 = zzbgm.zzg(parcel2, readInt);
                    break;
                default:
                    zzbgm.zzb(parcel2, readInt);
                    break;
            }
        }
        zzbgm.zzaf(parcel2, zzd);
        return new zzchp(str, i, s, d, d2, f, j, i2, i3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzchp[i];
    }
}
