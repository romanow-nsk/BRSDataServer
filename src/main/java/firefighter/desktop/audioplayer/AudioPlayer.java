/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop.audioplayer;

import firefighter.desktop.Cabinet;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author romanow
 */
public class AudioPlayer {
    private AudioInputStream audioInputStream=null;
    private AudioFormat format=null;
    private long pos=0;
    private Clip clip=null;
    private boolean isPlaying=false;
    private boolean isPause=false;
    private Thread loop=null;
    public boolean isPlaying(){
        return isPlaying && clip!=null;
        }
    public int getCurrentPlayTime(){
        return !isPlaying() ? 0 : (int)(clip.getMicrosecondPosition()/1000000);
        }
    public int getTotalPlayTime(){
        return !isPlaying() ? 0 : (int)(clip.getMicrosecondLength()/1000000);
    }
    public void resume(){
        if (clip==null)
            return;
        clip.setMicrosecondPosition(pos*1000000);
        isPause=false;
        clip.start();
        }
    public void pause() {
        if (clip!=null) {
            pos = (int)(clip.getMicrosecondPosition()/1000000);
            isPause=true;
            clip.stop();
            }
        }
    public void resume(int sec){
        if (clip==null)
            return;
        clip.stop();
        pos = sec;
        isPause=false;
        clip.setMicrosecondPosition(pos*1000000);
        clip.start();
        }
    public void stop(){
        if (loop!=null)
            loop.interrupt();
        isPlaying=false;
        if (clip!=null)
            clip.close();
        }
    public boolean startToPlay(URL PatnToFile,final PlayerCallBack back){
        stop();
        try {
            audioInputStream = AudioSystem.getAudioInputStream(PatnToFile);
            } catch (Exception e) {
                System.out.println("Player: "+e.toString());
                close();
                return false;
                }
        format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            close();
            System.out.println("Формат не поддерживается");
            return false;
            }
        try {                       // Для воспроизведения нужен отдельный аудиопоток
            clip = AudioSystem.getClip();
         	clip.open(AudioSystem.getAudioInputStream(PatnToFile));
            } catch (Exception ex) {
                System.out.println("Player: "+ex.toString());
                close();
                return false;
                }
        isPlaying=true;
        clip.setMicrosecondPosition(0);
        pos=0;
        clip.start();
        loop = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPlaying){
                try {
                    Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    if (isPause)
                        continue;
                    long pp = clip.getMicrosecondPosition();
                    if (!isPlaying || pp>=clip.getMicrosecondLength()){
                        isPlaying=false;
                        clip.close();
                        loop=null;
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                back.onEnd();
                            }
                        });
                        return;
                        }
                    pos = pp/1000000;
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            back.onPosition((int)pos);
                        }
                    });
                }
            }
        });
        loop.start();
        return true;
        }    
    public void close() {
        try {
            if (audioInputStream!=null)
                audioInputStream.close();
            if (clip!=null){
                clip.close();
                }
            } catch (IOException ex) {}
        }
}
