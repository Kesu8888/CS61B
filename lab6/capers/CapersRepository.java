package capers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        File capers = new File(".capers");
        if (!capers.exists()) {
            capers.mkdir();
        }
        File dogs = new File(".capers", "dogs");
        if (!dogs.exists()) {
            dogs.mkdir();
        }
        File story = new File(".capers", "story.txt");
        if (!story.exists()) {
            try {
                story.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        // TODO
        File inStory = new File(".capers", "story.txt");
        String StoryCont = Utils.readContentsAsString(inStory);
        if (StoryCont.length() == 0){
            Utils.writeContents(inStory, text);
        }
        else {
            Utils.writeContents(inStory, StoryCont + "\n" + text);
        }
        System.out.println(Utils.readContentsAsString(inStory));
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog dog = new Dog(name, breed, age);
        dog.saveDog();
        System.out.println(dog);
        // TODO
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog dogBirthday = Dog.fromFile(name);
        dogBirthday.haveBirthday();
        // TODO
    }
    public static void count() {
        Collection<File> files = Utils.
    }
}
