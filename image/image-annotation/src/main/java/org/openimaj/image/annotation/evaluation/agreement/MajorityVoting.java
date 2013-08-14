/**
 * 
 */
package org.openimaj.image.annotation.evaluation.agreement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openimaj.ml.annotation.ScoredAnnotation;

/**
 *	Majority voting.
 *
 *	@author David Dupplaw (dpd@ecs.soton.ac.uk)
 *  @created 14 Aug 2013
 *	@version $Author$, $Revision$, $Date$
 */
public class MajorityVoting
{
	/**
	 * 	Returns the basic majority vote or null if there is no majority
	 * 	within the given list of annotations.
	 * 
	 *	@param data The list of annotations to score
	 *	@return The majority annotation or NULL if there is no majority
	 */
	public static <A> ScoredAnnotation<A> calculateBasicMajorityVote(
			List<ScoredAnnotation<A>> data )
	{
		// Count all the different annotations.
		HashMap<A,Integer> count = new HashMap<A, Integer>();
		for( ScoredAnnotation<A> s : data )
		{
			if( count.get( s.annotation ) == null )
					count.put( s.annotation, 1 );
			else	count.put( s.annotation, count.get( s.annotation ) + 1 );
		}
		
		// Find the maximum annotation
		A majority = null;
		int max = 0;
		for( Entry<A, Integer> x : count.entrySet() )
		{
			// If we found a new max, we have a majority
			if( x.getValue() > max )
			{
				max = x.getValue();
				majority = x.getKey();				
			}
			else
			// If we found another one with the same count as max
			// then we no longer have a majority
			if( x.getValue() == max )
			{
				majority = null;
				// We mustn't reset max.
			}
		}
		
		// We'll return null if there's no majority
		if( majority == null )
			return null;
		
		// Otherwiser we return the majority annotation
		return new ScoredAnnotation<A>( majority, 
				count.get(majority)/(float)data.size() );
	}
	
	/**
	 * 	Calculated the majority vote for a set of subjects, where each
	 * 	subject has a set of answers. Note that as the majority voting
	 * 	method may return null in the case of no majority, it is possible
	 * 	that some of the subjects in the return of this method will have a
	 * 	null map.
	 * 
	 *	@param data The data
	 *	@return The subjects mapped to their majority vote.
	 */
	public static <A> Map<String, ScoredAnnotation<A>> 
		calculateBasicMajorityVote( Map<String,List<ScoredAnnotation<A>>> data )
	{
		Map<String,ScoredAnnotation<A>> out = 
				new HashMap<String, ScoredAnnotation<A>>();
		
		for( Entry<String, List<ScoredAnnotation<A>>> x : data.entrySet() )
			out.put( x.getKey(), calculateBasicMajorityVote( x.getValue() ) );
			
		return out;
	}
}
