package me.springroll.bot;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.DataTruncation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Created by Jacob on 4/23/2017.
 */
public class MyListener extends ListenerAdapter{

    public static BufferedImage MsgImg;
    public static Graphics2D mG;
    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
    LocalDate dateNow = LocalDate.now();
        Message message = event.getMessage();
        String content = message.getRawContent();
        long msgID = message.getIdLong();
        MessageChannel channel = event.getChannel();
        String MsgChannel = "";
        if(message.getChannelType()== ChannelType.PRIVATE){
            MsgChannel = "DirectMessage-";
        }else if(message.getChannelType()==ChannelType.GROUP){
            MsgChannel = "Group-";
        }else if(message.getChannelType()==ChannelType.TEXT){

        }else{

        }
        if(new File("Logs\\"+message.getGuild().getName()+"-"+channel.getName()+"_"+dateFormat.format(dateNow)+".txt").exists() && message.getChannelType()==ChannelType.TEXT ){
           //do nothing
        }else{
            try{
                boolean temp = new File("Logs\\"+message.getGuild().getName()+"-"+channel.getName()+"_"+dateFormat.format(dateNow)+".txt").createNewFile();
                System.out.println("Logs\\"+message.getGuild().getName()+"-"+channel.getName()+"_"+dateFormat.format(dateNow)+".txt");
                System.out.println(temp);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try{
            PrintWriter FWriter = new PrintWriter(new FileWriter("Logs\\"+message.getGuild().getName()+"-"+channel.getName()+"_"+dateFormat.format(dateNow)+".txt",true));
            FWriter.println(message.getAuthor().toString().substring(2)+": "+message.getCreationTime().toString().substring(0,10)+" "+message.getCreationTime().toString().substring(10)+": \""+content+"\"");
            FWriter.flush();
            FWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(event.getAuthor().isBot() || !(event.getAuthor().getId().equals("118208118629990403"))) return;


        if(content.startsWith("<")){

            channel.deleteMessageById(msgID).queue();

            if(content.equals("<emotelist")){
                try{
                    StringBuilder res = new StringBuilder();
                    Arrays.asList(new File("res//").listFiles()).forEach(file ->
                            res.append(file.getName().substring(0,file.getName().lastIndexOf('.'))+", "));
                    channel.sendMessage("Emote List: "+res.toString().substring(0,res.toString().lastIndexOf(','))).queue();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            if(content.contains(",")){
                int numcomma=0;
                for(int i=0;i<content.length();i++){
                    if(content.charAt(i)==',') numcomma++;
                }
                String[] emotes = new String[numcomma+1];
                for (int i=0;i<emotes.length;i++){
                    emotes[i]="";
                }
                int count=0;
                for(int i=1;i<content.length();i++){
                    if(content.charAt(i)==','){
                        count++;
                    }else{
                        emotes[count]+=content.charAt(i);
                    }
                }
                boolean valid = true;
                int width=0;
                int height=0;
                for(int i=0;i<emotes.length;i++){
                    if( !(new File("res\\" + emotes[i].toLowerCase()+".png").exists())){
                        valid=false;
                        channel.sendMessage("Image "+"res\\" + emotes[i].toLowerCase()+".png"+" doesnt exist").queue();
                    }else{
                        BufferedImage temp;
                        try {
                            temp = ImageIO.read(new File("res\\" + emotes[i].toLowerCase() + ".png"));
                            width+= temp.getWidth();
                            if(height<temp.getHeight()) height=temp.getHeight();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(valid){
                MsgImg = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
                mG = MsgImg.createGraphics();
                int dwidth = 0;
                for(int i=0;i<emotes.length;i++) {
                    try {
                        mG.drawImage(ImageIO.read(new File("res\\" + emotes[i].toLowerCase() + ".png")), null,dwidth,height-ImageIO.read(new File("res\\" + emotes[i].toLowerCase() + ".png")).getHeight() );
                        dwidth+=ImageIO.read(new File("res\\" + emotes[i].toLowerCase() + ".png")).getWidth();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                    try {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(MsgImg,"png",os);
                        channel.sendFile(os.toByteArray(), "Emote.png",null).queue();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else return;
            }

            if(new File("res\\" + content.substring(1, content.length()).toLowerCase()+".png").exists()) {

                    channel.sendFile(new File("res\\" + content.substring(1, content.length()).toLowerCase() + ".png"), null).queue();
                        channel.sendMessage("```IOException error, check console for more information```");

            }
        }

    }

}

/**/