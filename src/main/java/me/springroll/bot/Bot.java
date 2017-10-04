package me.springroll.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.File;

/**
 * Created by Jacob on 4/23/2017.
 */
public class Bot {

    public static void main (String[] args) throws InterruptedException, RateLimitedException, LoginException{

        JDABuilder mJBuilder = new JDABuilder(AccountType.CLIENT).setToken("MTE4MjA4MTE4NjI5OTkwNDAz.C3Kmsg.mkdK5fC1O1-zVHiyVmCKb1aqDRc");
        mJBuilder.addEventListener(new MyListener());
        mJBuilder.buildBlocking();
    }

}
