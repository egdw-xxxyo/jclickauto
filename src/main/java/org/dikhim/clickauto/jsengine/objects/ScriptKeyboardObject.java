package org.dikhim.clickauto.jsengine.objects;

import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.utils.KeyCodes;
import org.dikhim.clickauto.jsengine.utils.typer.Typer;
import org.dikhim.clickauto.jsengine.utils.typer.Typers;
import org.dikhim.clickauto.util.MathUtil;
import org.dikhim.clickauto.util.Out;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ScriptKeyboardObject implements KeyboardObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final double MULTIPLIER = 1f;
    private final int MIN_DELAY = 5;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private double multiplier = MULTIPLIER;
    private int minDelay = MIN_DELAY;


    private Robot robot;
    private final Object monitor;

    public ScriptKeyboardObject(Robot robot) {
        this.robot = robot;
        this.monitor = robot.getMonitor();
    }

    @Override
    public int getMinDelay() {
        synchronized (monitor) {
            return minDelay;
        }
    }

    @Override
    public int getMultipliedPressDelay() {
        synchronized (monitor) {
            return checkDelay((int) (pressDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedReleaseDelay() {
        synchronized (monitor) {
            return checkDelay((int) (releaseDelay * multiplier));
        }
    }

    @Override
    public double getMultiplier() {
        synchronized (monitor) {
            return multiplier;
        }
    }

    @Override
    public int getPressDelay() {
        synchronized (monitor) {
            return pressDelay;
        }
    }

    @Override
    public int getReleaseDelay() {
        synchronized (monitor) {
            return releaseDelay;
        }
    }

    @Override
    public double getSpeed() {
        synchronized (monitor) {
            if (multiplier == 0) return 999999999;
            return MathUtil.roundTo1(1.0 / multiplier);
        }
    }


    @Override
    public void perform(String keys, String action) {
        synchronized (monitor) {
            switch (action) {
                case "PRESS":
                    press(keys);
                    break;
                case "RELEASE":
                    release(keys);
                    break;
                case "TYPE":
                    type(keys);
                default:
                    Out.println(String.format("Undefined key actions '%s' in perform method", action));
            }
        }
    }

    @Override
    public void press(String keys) {
        synchronized (monitor) {
            Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
            for (String key : keySet) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyPress(keyCode);
                    delay(getMultipliedPressDelay());
                } else {
                    Out.println(String.format("Undefined key '%s'in sequence '%s' in press method", key, keys));
                }
            }
        }
    }

    @Override
    public void release(String keys) {
        synchronized (monitor) {
            Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
            for (String key : keySet) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyRelease(keyCode);
                    delay(getMultipliedReleaseDelay());
                } else {
                    Out.println(String.format("Undefined key '%s' in release method", key));
                }
            }
        }
    }

    @Override
    public void resetDelays() {
        synchronized (monitor) {
            this.pressDelay = PRESS_DELAY;
            this.releaseDelay = RELEASE_DELAY;
        }
    }

    @Override
    public void resetMultiplier() {
        synchronized (monitor) {
            this.multiplier = MULTIPLIER;
        }
    }

    @Override
    public void resetSpeed() {
        synchronized (monitor) {
            resetMultiplier();
        }
    }

    @Override
    public void setDelays(int delay) {
        synchronized (monitor) {
            setPressDelay(delay);
            setReleaseDelay(delay);
        }
    }

    @Override
    public void setMinDelay(int delay) {
        synchronized (monitor) {
            this.minDelay = delay;
        }
    }

    @Override
    public void setMultiplier(double multiplier) {
        synchronized (monitor) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
        }
    }

    @Override
    public void setPressDelay(int pressDelay) {
        synchronized (monitor) {
            if (pressDelay < 0) {
                this.pressDelay = 0;
            } else {
                this.pressDelay = pressDelay;
            }
        }
    }

    @Override
    public void setReleaseDelay(int releaseDelay) {
        synchronized (monitor) {
            if (releaseDelay < 0) {
                this.releaseDelay = 0;
            } else {
                this.releaseDelay = releaseDelay;
            }
        }
    }

    @Override
    public void setSpeed(double speed) {
        synchronized (monitor) {
            if (speed < 0.1) {
                speed = 0.1;
            }
            speed = MathUtil.roundTo1(speed);
            setMultiplier(1f / speed);
        }
    }

    @Override
    public void type(String keys) {
        synchronized (monitor) {
            String[] keyList = keys.split(" ");
            for (String key : keyList) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyPress(keyCode);
                    delay(getMultipliedPressDelay());
                    robot.keyRelease(keyCode);
                    delay(getMultipliedReleaseDelay());
                } else {
                    Out.println(String.format("Undefined key '%s' in type method", key));
                }
            }
        }
    }

    @Override
    public void typeText(String layout, String text) {
        try {
            Typer typer = Typers.create(this, layout);
            typer.type(text);
        } catch (Exception e) {
            Out.println(e.getMessage());
        }
    }

    private int checkDelay(int delay) {
        if (delay < minDelay) return minDelay;

        return delay;
    }

    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}