package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class PlayerCollisions {
    
    public static ContactListener createCollisionListener() {
        ContactListener listener = new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();
                Fixture travis = null;
                Fixture other = null;
                Fixture falling = null;

                if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null) {
                    if (fixA.getBody().getUserData().toString() == "falling") {
                        falling = fixA;
                        other = fixB;
                    } else if (fixB.getBody().getUserData().toString() == "falling") {
                        falling = fixB;
                        other = fixA;
                    }
                    /*
                    if (falling != null && falling.getBody().getUserData() instanceof FallingTile) {
                        if (falling.untouched && other.getBody().getUserData().toString() == "Travis") {
                            falling.fall();
                        } else if (!falling.untouched && (other.getBody().getUserData() instanceof Tile) && (!other.getBody().getUserData() instanceof Spike) && (!other.getBody().getUserData() instanceof GravityUp)) {
                            falling.poof();
                        }
                    }
                    */
                    if (fixA.getBody().getUserData().toString() == "Travis") {
                        travis = fixA;
                        other = fixB;
                    } else if (fixB.getBody().getUserData().toString() == "Travis") {
                        travis = fixB;
                        other = fixA;
                    }

                    if (travis != null && travis.getBody().getUserData() instanceof Player) {
                        Player trueTravis = (Player) travis.getBody().getUserData();
                        Tile trueOther = (Tile) other.getBody().getUserData();
                        if (trueOther.label != "sticky" && (trueOther.getPosition().x - trueTravis.pos.x <= 0.2f)) {
                            trueTravis.sticky = false;
                        }
                        if (trueOther.label != "falling" && (trueOther.getPosition().x - trueTravis.pos.x <= 0.2f)) {
                            trueTravis.landedOnFalling = false;
                            System.out.println("Collission Listener - Landed on Falling: "+trueTravis.landedOnFalling);
                        }
                        if (trueOther.label != "bouncy" && (trueTravis.gravityDirection == "up" && trueOther.getPosition().y - trueTravis.pos.y > 0) || (trueTravis.gravityDirection == "down" && trueTravis.pos.y - trueOther.getPosition().y > 0)) {
                            trueTravis.landed = true;
                            System.out.println("Collision Listener - Landed not on bouncy   : "+trueTravis.landed);
                        }
                        switch (trueOther.label) {
                            case "starttile" :
                                trueTravis.level.changeRoom = -1;
                                System.out.println("changeRoom: "+trueTravis.level.changeRoom);
                            break;
                            case "endtile" :
                                trueTravis.level.changeRoom = 1;
                                System.out.println("changeRoom: "+trueTravis.level.changeRoom);
                            break;
                            case "sticky" :
                                trueTravis.sticky = true;
                                System.out.println("Such stick,  Wow");
                            break;
                            case "bouncy" :
                                System.out.println("Bounce dif: "+ (trueOther.getPosition().y - trueTravis.pos.y));
                                if (trueOther.rotation == 0 && (trueOther.getPosition().y - trueTravis.pos.y <= 0.34f)) {
                                    trueTravis.bounce("up");
                                    System.out.println("Boing!");
                                } else if (trueOther.rotation == 180 && (trueOther.getPosition().y - trueTravis.pos.y >= 0.34f)) {
                                    trueTravis.bounce("down");
                                    System.out.println("Boing!");
                                }
                            break;
                            case "falling" :
                                trueTravis.landedOnFalling = true;
                                if (trueOther instanceof FallingTile) {
                                    FallingTile fallingOther = (FallingTile)trueOther;
                                    fallingOther.playerLandInit();
                                }   
                            break;
                            case "sliderleft" :
                                //trueTravis.sliderleft;
                                System.out.println("<-----");
                            break;
                            case "sliderright" :
                                //trueTravis.sliderright;
                                System.out.println("----->");
                            break; 
                            case "gravityup" :
                                trueTravis.gravityDirection = "up";
                                trueTravis.resumeGravityDown = -1;
                                System.out.println("Up We Go");
                            break;
                            case "spike" :
                                /*
                                //call reset method?
                                System.out.println("about to reset stage");
                                if (trueTravis.level instanceof Level1) {
                                    System.out.println("instance good");
                                    trueTravis.level.resetStage();
                                }*/
                                trueTravis.level.resetFromSpikeCollision = true;
                                System.out.println("Ouch!");
                            break;

                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();
                Fixture travis = null;
                Fixture other = null;
                System.out.println("End contact");


                if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null) {
                    if (fixA.getBody().getUserData().toString() == "Travis") {
                        travis = fixA;
                        other = fixB;
                    } else if (fixB.getBody().getUserData().toString() == "Travis") {
                        travis = fixB;
                        other = fixA;
                    }

                    

                    if (travis != null && travis.getBody().getUserData() instanceof Player) {
                        Player trueTravis = (Player) travis.getBody().getUserData();
                        Tile trueOther = (Tile) other.getBody().getUserData();
                        switch (trueOther.label) {
                            case "starttile" :
                                trueTravis.level.changeRoom = 0;
                                System.out.println("changeRoom: "+trueTravis.level.changeRoom);
                            break;
                            case "endtile" :
                                trueTravis.level.changeRoom = 0;
                                System.out.println("changeRoom: "+trueTravis.level.changeRoom);
                            break;
                            case "sticky" :
                                //trueTravis.sticky = false;
                                System.out.println("No more stick");
                            break;
                            //bouncy only needs to get called once, I suspect, and doesn't need an end condition
                            //case "bouncy" :
                                //trueTravis.bounce();
                            //    System.out.println("Boing!");
                            //break;
                            case "sliderleft" :
                                //trueTravis.sliderleft;
                                System.out.println("<--x--");
                            break;
                            case "sliderright" :
                                //trueTravis.sliderright;
                                System.out.println("--x-->");
                            break;
                            case "gravityup" :
                                trueTravis.resumeGravityDown = 2;
                                System.out.println("Fall timer reset");
                            break;
                            //since the case resets, and probably calls a method, it shouldnt need an end case
                            //case "spike" :
                                //call reset method?
                            //    System.out.println("Ouch!");
                            //break;

                        }
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //Don't know what this does, but it has to be here to implement ContactListener
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                //Don't know what this does, but it has to be here to implement ContactListener
            }
        };
        return listener;
        }

}
