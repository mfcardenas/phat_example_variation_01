package phat.beans;

import java.io.Serializable;

public class SimSensorEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private float[] accel;

    private float[] gyro;
    private float temp;

    private int type;
    private int accuracy;
    private long step;

    private float ax;
    private float ay;
    private float az;

    private float gx;
    private float gy;
    private float gz;

    public SimSensorEvent(int type, float[] accel, float ax, float ay, float az, long step, int accuracy) {
        this.setType(type);
        this.setAccel(accel);
        this.setStep(step);
        this.setAccuracy(accuracy);
        this.setAx(ax);
        this.setAy(ay);
        this.setAz(az);
    }

    public float[] getAccel() {
        return this.accel;
    }

    public int getType() {
        return this.type;
    }

    public long getStep() {
        return this.step;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public String toString() {
        super.toString();
        String result = "simsensorevent;" + this.getStep() + ";";
        switch(this.getType()) {
            case 1:
                result = result + "TYPE_ACCELEROMETER";
                break;
            case 2:
                result = result + "TYPE_MAGNETIC_FIELD";
                break;
            case 3:
                result = result + "TYPE_ORIENTATION";
        }

        result = result + ";" + this.getAccuracy() + ";" + this.getAccel()[0] + ";" + this.getAccel()[1] + ";" + this.getAccel()[2];
        return result;
    }

    public static SimSensorEvent fromString(String input) {
        if (input == null) {
            return null;
        } else {
            String[] fields = input.split(";");
            if (fields.length != 7) {
                return null;
            } else {
                float x = Float.parseFloat(fields[4]);
                float y = Float.parseFloat(fields[5]);
                float z = Float.parseFloat(fields[6]);
                int accuracy = Integer.parseInt(fields[3]);
                int type = 0;
                String var7 = fields[2];
                byte var8 = -1;
                switch(var7.hashCode()) {
                    case -953143676:
                        if (var7.equals("TYPE_MAGNETIC_FIELD")) {
                            var8 = 1;
                        }
                        break;
                    case -179702806:
                        if (var7.equals("TYPE_ACCELEROMETER")) {
                            var8 = 0;
                        }
                        break;
                    case 624388139:
                        if (var7.equals("TYPE_ORIENTATION")) {
                            var8 = 2;
                        }
                }

                switch(var8) {
                    case 0:
                        type = 1;
                        break;
                    case 1:
                        type = 2;
                        break;
                    case 2:
                        type = 3;
                }

                int step = Integer.parseInt(fields[1]);
                return new SimSensorEvent(type, new float[]{x, y, z}, x, y, z, (long)step, accuracy);
            }
        }
    }

    public void setAccel(float[] accel) {
        this.accel = accel;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public float getAx() {
        return ax;
    }

    public void setAx(float ax) {
        this.ax = ax;
    }

    public float getAy() {
        return ay;
    }

    public void setAy(float ay) {
        this.ay = ay;
    }

    public float getAz() {
        return az;
    }

    public void setAz(float az) {
        this.az = az;
    }

    public float getGx() {
        return gx;
    }

    public void setGx(float gx) {
        this.gx = gx;
    }

    public float getGy() {
        return gy;
    }

    public void setGy(float gy) {
        this.gy = gy;
    }

    public float getGz() {
        return gz;
    }

    public void setGz(float gz) {
        this.gz = gz;
    }
}
