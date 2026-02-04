package com.example.virtualcompanion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper
 *
 * This class:
 * - Creates the database
 * - Creates all tables
 * - Handles upgrades
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database file name
    private static final String DB_NAME = "virtual_companion.db";

    // Change this if you modify tables later
    private static final int DB_VERSION = 8; // Incremented for accessory table

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Called automatically when DB is created first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ================= USER TABLE =================
        // Stores main player data
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS user (" +

                        // Unique ID (auto-generated)
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        // User / pet name (cannot be empty)
                        "name TEXT NOT NULL, " +
                        // User coins (default 0)
                        "coins INTEGER NOT NULL DEFAULT 0, " +
                        // Pet gender (only allowed values)
                        "pet_gender TEXT NOT NULL CHECK " +
                        "(pet_gender IN ('male','female'))" +
                        ");"
        );

        // ================= ACCESSORY TABLE =================
        // Stores shop & equipped items
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS accessory (" +

                        // Unique ID
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        // Drawable resource ID
                        "image INTEGER NOT NULL, " +
                        // Item price
                        "price INTEGER NOT NULL, " +
                        // Category
                        "type TEXT NOT NULL CHECK " +
                        "(type IN ('top','bottom','hat','glasses')), " +
                        // 0 = not owned, 1 = owned
                        "owned INTEGER NOT NULL DEFAULT 0 CHECK (owned IN (0,1)), " +
                        // 0 = not equipped, 1 = equipped
                        "equipped INTEGER NOT NULL DEFAULT 0 CHECK (equipped IN (0,1))" +
                        ");"
        );

        // ================= QUEST TABLE =================
        // Stores quest progress
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS quest (" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        // Quest title
                        "title TEXT NOT NULL, " +
                        // Quest description
                        "description TEXT, " +
                        // Coins reward
                        "reward INTEGER NOT NULL DEFAULT 0, " +
                        // Timer duration in minutes
                        "timer_minutes INTEGER NOT NULL DEFAULT 5, " +
                        "progress INTEGER NOT NULL DEFAULT 0, " +
                        // 0 = not done, 1 = done
                        "rewarded INTEGER NOT NULL DEFAULT 0 CHECK (rewarded IN (0,1)), " +
                        // Mood category (neutral, happy, sad, angry, anxious)
                        "mood TEXT NOT NULL CHECK " +
                        "(mood IN ('neutral','happy','sad','angry','anxious'))" +
                        ");"
        );

        // ================= MOOD TABLE =================
        // Stores mood history
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS mood (" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        // Mood value (1 to 5 only)
                        "value INTEGER NOT NULL CHECK (value BETWEEN 1 AND 5), " +
                        // Date string
                        "date TEXT NOT NULL" +
                        ");"
        );

        // Insert default values
        insertDefaults(db);
    }

    /**
     * Insert starting data (runs once)
     */
    private void insertDefaults(SQLiteDatabase db) {

        db.execSQL(
                "INSERT OR IGNORE INTO user (id, name, coins, pet_gender) " +
                        "VALUES (1,'',150,'male');"
        );


        db.execSQL(
                "INSERT OR IGNORE INTO quest (title, description, reward, timer_minutes, mood) VALUES " +

                        // ========== NEUTRAL (21 quests) - 1-2 min ==========
                        "('Calm Breathing','Breathe slowly. In through your nose, out through your mouth. Do this 10 times.',30,1,'neutral')," +
                        "('Sip Water','Drink a glass of water slowly. Feel the coolness.',30,1,'neutral')," +
                        "('Three Good Things','Think of 3 things that went well today. Write them down if you like.',40,2,'neutral')," +
                        "('Neck Stretch','Gently tilt your head side to side. Relax your shoulders.',30,1,'neutral')," +
                        "('Walk Around','Walk around your room or home slowly. Notice what you see.',40,2,'neutral')," +
                        "('Tidy Up','Put away 5 things that are out of place. Take your time.',40,2,'neutral')," +
                        "('Rest Your Eyes','Close your eyes. Let them rest. Breathe calmly.',30,1,'neutral')," +
                        "('Shoulder Rolls','Roll your shoulders back gently 10 times. Feel the release.',30,1,'neutral')," +
                        "('Touch Something Soft','Find something soft like a pillow or blanket. Hold it.',30,1,'neutral')," +
                        "('Eat Mindfully','Eat a small snack slowly. Taste each bite.',40,2,'neutral')," +
                        "('Hand Massage','Rub your hands together. Massage your fingers gently.',30,1,'neutral')," +
                        "('Stand Tall','Stand up straight. Roll your shoulders back. Take a deep breath.',30,1,'neutral')," +
                        "('Look Outside','Look out a window. Notice the sky, trees, or buildings.',40,2,'neutral')," +
                        "('Relax Your Face','Unclench your jaw. Relax your forehead. Soften your eyes.',30,1,'neutral')," +
                        "('Warm Drink','Make tea or warm water. Sip it slowly.',40,2,'neutral')," +
                        "('Listen to Music','Play one calm song. Just listen.',40,2,'neutral')," +
                        "('Draw Freely','Doodle on paper. Draw whatever comes to mind.',40,2,'neutral')," +
                        "('Body Check','Notice where you feel tight. Breathe into that spot.',40,2,'neutral')," +
                        "('Arm Stretch','Reach your arms up high. Stretch side to side.',30,1,'neutral')," +
                        "('One Small Task','Do one tiny task you have been putting off.',30,1,'neutral')," +
                        "('Kind Words','Say something nice to yourself. You deserve it.',30,1,'neutral')," +

                        // ========== HAPPY (21 quests) - 1-2 min ==========
                        "('Share Your Joy','Text someone about something that made you smile today.',30,1,'happy')," +
                        "('Dance Freely','Put on a happy song and dance however you want.',40,2,'happy')," +
                        "('Smile at Yourself','Look in the mirror and smile at yourself. You are doing great.',30,1,'happy')," +
                        "('Sing Along','Sing your favorite song out loud. Be as loud as you want.',40,2,'happy')," +
                        "('Say Something Nice','Tell someone something kind. It can be in person or by text.',30,1,'happy')," +
                        "('Remember Happiness','Think about a happy memory. Let yourself feel that joy again.',40,2,'happy')," +
                        "('Watch Something Funny','Watch a short funny video. Let yourself laugh.',40,2,'happy')," +
                        "('Celebrate Yourself','Do a little dance. You have done something good today.',30,1,'happy')," +
                        "('Fresh Air','Open a window or step outside. Feel the air on your skin.',30,1,'happy')," +
                        "('Favorite Song','Play a song that makes you happy. Sing or hum along.',40,2,'happy')," +
                        "('Thank Someone','Tell someone thank you for something they did.',30,1,'happy')," +
                        "('Move Your Body','Do 10 jumping jacks or march in place. Feel the energy.',30,1,'happy')," +
                        "('Write Happiness','Write one sentence about what made you happy today.',30,1,'happy')," +
                        "('Look at Cute Animals','Look at photos or videos of cute animals. Enjoy the cuteness.',40,2,'happy')," +
                        "('Enjoy a Treat','Have a small treat you like. Enjoy every bite.',30,1,'happy')," +
                        "('Do Something Kind','Do one small kind thing for yourself or someone else.',40,2,'happy')," +
                        "('Victory Pose','Stand with your arms up high. Feel strong and proud.',30,1,'happy')," +
                        "('Call Someone','Call someone you care about. Say hello.',40,2,'happy')," +
                        "('Plan Something Fun','Think about something fun you want to do this week.',30,1,'happy')," +
                        "('Pat Yourself','Give yourself a pat on the back. You are doing well.',30,1,'happy')," +
                        "('Capture Joy','Take a photo of something that makes you happy right now.',30,1,'happy')," +

                        // ========== SAD (21 quests) - 1-2 min ==========
                        "('Talk to Someone','Text or call someone you trust. Tell them how you feel.',40,2,'sad')," +
                        "('Gentle Breathing','Breathe in slowly for 4 counts. Breathe out slowly for 6 counts. Repeat.',30,1,'sad')," +
                        "('Write It Out','Write down how you are feeling right now. Do not hold back.',40,2,'sad')," +
                        "('Comforting Song','Listen to a song that comforts you. Let it soothe you.',40,2,'sad')," +
                        "('Slow Walk','Walk slowly around your space. Be gentle with yourself.',40,2,'sad')," +
                        "('Hug Yourself','Wrap your arms around yourself. Give yourself a gentle hug.',30,1,'sad')," +
                        "('Let It Out','If you need to cry, let yourself cry. It is okay.',30,1,'sad')," +
                        "('Be Kind to You','Say one kind thing to yourself. You deserve kindness.',30,1,'sad')," +
                        "('Get Cozy','Wrap yourself in a soft blanket or put on cozy clothes.',30,1,'sad')," +
                        "('Warm Water','Splash warm water on your face. It can feel soothing.',30,1,'sad')," +
                        "('Look at Memories','Look at a photo from a happy time. Remember that feeling.',30,1,'sad')," +
                        "('Comfort Food','Make yourself something warm or comforting to eat.',40,2,'sad')," +
                        "('Soft Music','Play gentle, quiet music. Let it calm you.',40,2,'sad')," +
                        "('Hold Something Soft','Hold a pillow, stuffed animal, or soft blanket. Squeeze it gently.',30,1,'sad')," +
                        "('Read Something Kind','Read a kind quote or message. Let it comfort you.',30,1,'sad')," +
                        "('Safe Spot','Sit somewhere you feel safe and comfortable. Just be.',30,1,'sad')," +
                        "('Think of Hope','Think of one thing you can look forward to, even if it is small.',30,1,'sad')," +
                        "('Gentle Stretching','Do slow, gentle stretches. Be soft with your body.',40,2,'sad')," +
                        "('Reach Out','Send a message to someone who cares about you.',30,1,'sad')," +
                        "('Pet Comfort','If you have a pet, spend time with them. Or look at cute animal photos.',30,1,'sad')," +
                        "('Remember Your Strength','Think about a hard time you got through before. You did it once. You can do it again.',40,2,'sad')," +

                        // ========== ANGRY (21 quests) - 1-2 min ==========
                        "('Strong Breathing','Breathe in hard through your nose. Breathe out hard through your mouth. Do this 10 times.',30,1,'angry')," +
                        "('Move Fast','Run in place or do fast steps for 1 minute. Let the energy out.',30,1,'angry')," +
                        "('Hit a Pillow','Punch or hit a pillow as hard as you need to. It is safe.',30,1,'angry')," +
                        "('Scream Safely','Scream into a pillow. Let the sound out.',30,1,'angry')," +
                        "('Write Your Anger','Write down exactly why you are angry. Do not hold back.',40,2,'angry')," +
                        "('Cold Water','Splash cold water on your face or hands. Feel the shock.',30,1,'angry')," +
                        "('Count Backwards','Count backwards from 50. Focus on the numbers.',30,1,'angry')," +
                        "('Loud Music','Put on loud, intense music. Let it match your energy.',40,2,'angry')," +
                        "('Scrub Something','Scrub a dish, table, or surface hard. Put the energy into cleaning.',40,2,'angry')," +
                        "('Punch the Air','Do boxing punches in the air. Imagine hitting your anger.',30,1,'angry')," +
                        "('Hold Ice','Hold an ice cube in your hand. Feel the cold.',30,1,'angry')," +
                        "('Stomp Around','Stomp your feet hard. Make noise. Let it out.',30,1,'angry')," +
                        "('Rip Paper','Rip up old paper or cardboard. Tear it as much as you need.',30,1,'angry')," +
                        "('Say Why','Say out loud why you are angry. No one has to hear but you.',40,2,'angry')," +
                        "('Tighten and Release','Tighten all your muscles hard. Then let go.',30,1,'angry')," +
                        "('Step Away','Walk away from what made you angry. Take a break.',40,2,'angry')," +
                        "('Picture Calmness','Picture a calm, peaceful place. Try to feel it.',40,2,'angry')," +
                        "('Record Yourself','Record yourself talking about your anger. Delete it after if you want.',40,2,'angry')," +
                        "('Push-Ups','Do 10 push-ups or wall push-ups. Use the anger as fuel.',30,1,'angry')," +
                        "('Plan a Solution','Write down one thing you can do to fix the problem.',40,2,'angry')," +
                        "('Jump Around','Do 10 jumping jacks or jump in place. Move that energy out.',30,1,'angry')," +

                        // ========== ANXIOUS (21 quests) - 1-2 min ==========
                        "('4-7-8 Breathing','Breathe in for 4 counts. Hold for 7. Breathe out for 8. Do this 3 times.',30,1,'anxious')," +
                        "('Name 5 Things','Name 5 things you can see. 4 you can hear. 3 you can touch. 2 you can smell. 1 you can taste.',40,2,'anxious')," +
                        "('Write Your Worries','Write down every worry in your head. Get them all out.',40,2,'anxious')," +
                        "('Body Check','Notice where you feel tense. Your jaw? Shoulders? Stomach? Just notice.',40,2,'anxious')," +
                        "('Calming Sounds','Listen to rain sounds, ocean waves, or white noise.',40,2,'anxious')," +
                        "('Name the Anxiety','Write down what is making you anxious right now.',40,2,'anxious')," +
                        "('Tense and Relax','Squeeze your fists tight. Then let go. Do this with your shoulders too.',40,2,'anxious')," +
                        "('Safe Place','Picture a place where you feel totally safe. Imagine being there.',40,2,'anxious')," +
                        "('Sip Warm Tea','Make warm tea or water with honey. Sip it slowly.',40,2,'anxious')," +
                        "('Skip Caffeine','Drink water instead of coffee or soda. Your body will thank you.',30,1,'anxious')," +
                        "('Remember Coping','Think of a time you felt anxious before. How did you get through it?',30,1,'anxious')," +
                        "('Reality Check','Ask yourself: Is this thought true? What is the evidence?',40,2,'anxious')," +
                        "('Walk Slowly','Walk very slowly. Focus on each step. Feel your feet on the ground.',40,2,'anxious')," +
                        "('Press Your Palms','Press your palms together hard. Hold for 30 seconds. Feel the pressure.',30,1,'anxious')," +
                        "('Use Weight','Put a heavy blanket on you or press down on your legs. Feel grounded.',30,1,'anxious')," +
                        "('Smell Something','Smell something calming like lavender, soap, or fresh air.',30,1,'anxious')," +
                        "('Repeat a Phrase','Say to yourself: I am safe. I am okay. I can handle this. Repeat 10 times.',30,1,'anxious')," +
                        "('Color a Shape','Color in one simple shape or pattern. Focus only on that.',40,2,'anxious')," +
                        "('One Small Step','Write down one tiny step you can take to handle your worry.',40,2,'anxious')," +
                        "('Text Support','Text someone who supports you. You do not have to explain everything.',30,1,'anxious')," +
                        "('Count Challenge','Count backwards from 100 by 7s. Focus only on counting.',40,2,'anxious');" +
                        ""
        );

        // Note: Accessory items can be added here if you have a shop system
        // For now, the table exists but is empty
    }

    /**
     * Handle database upgrades
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Add timer column if upgrading from version 6 to 7
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE quest ADD COLUMN timer_minutes INTEGER NOT NULL DEFAULT 5");
        }

        // Add accessory table if upgrading to version 8
        if (oldVersion < 8) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS accessory (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "image INTEGER NOT NULL, " +
                            "price INTEGER NOT NULL, " +
                            "type TEXT NOT NULL CHECK " +
                            "(type IN ('top','bottom','hat','glasses')), " +
                            "owned INTEGER NOT NULL DEFAULT 0 CHECK (owned IN (0,1)), " +
                            "equipped INTEGER NOT NULL DEFAULT 0 CHECK (equipped IN (0,1))" +
                            ");"
            );
        }
    }
}